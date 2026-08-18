package br.com.controlefinanceiro.mslancamentos.service;

import br.com.controlefinanceiro.mslancamentos.client.CentroCustoClient;
import br.com.controlefinanceiro.mslancamentos.client.ContaClient;
import br.com.controlefinanceiro.mslancamentos.dto.EfetivarRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoBancarioRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoParceladoRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoResponseDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoVencimentoDTO;
import br.com.controlefinanceiro.mslancamentos.entity.CartaoCredito;
import br.com.controlefinanceiro.mslancamentos.entity.Lancamento;
import br.com.controlefinanceiro.mslancamentos.enums.FormaPagamento;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoMovimentoBancario;
import br.com.controlefinanceiro.mslancamentos.exception.LancamentoNaoAutorizadoException;
import br.com.controlefinanceiro.mslancamentos.exception.LancamentoNaoEncontradoException;
import br.com.controlefinanceiro.mslancamentos.repository.CartaoCreditoRepository;
import br.com.controlefinanceiro.mslancamentos.repository.LancamentoRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final CentroCustoClient centroCustoClient;
    private final ContaClient contaClient;
    private final HttpServletRequest request;

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String getToken() {
        return request.getHeader("Authorization");
    }

    private void validarCentroCusto(Long centroCustoId) {
        if (centroCustoId == null) return;
        try {
            centroCustoClient.buscarPorId(centroCustoId, getToken());
        } catch (FeignException.NotFound e) {
            throw new LancamentoNaoEncontradoException("Centro de custo não encontrado ou não pertence ao usuário");
        }
    }

    private void validarCartaoCredito(FormaPagamento formaPagamento, Long cartaoCreditoId, String email) {
        if (formaPagamento != FormaPagamento.CARTAO_CREDITO) return;
        if (cartaoCreditoId == null) {
            throw new LancamentoNaoAutorizadoException("Cartão de crédito é obrigatório para esta forma de pagamento");
        }
        cartaoCreditoRepository
                .findByIdAndUsuarioIdAndAtivoTrue(cartaoCreditoId, email)
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Cartão de crédito não encontrado"));
    }

    private BigDecimal calcularValor(BigDecimal valorOriginal, BigDecimal juros, BigDecimal desconto) {
        BigDecimal j = juros != null ? juros : BigDecimal.ZERO;
        BigDecimal d = desconto != null ? desconto : BigDecimal.ZERO;
        return valorOriginal.add(j).subtract(d);
    }

    public LancamentoResponseDTO criar(LancamentoRequestDTO dto) {
        String email = getEmailAutenticado();
        validarCentroCusto(dto.centroCustoId());
        validarCartaoCredito(dto.formaPagamento(), dto.cartaoCreditoId(), email);

        Lancamento lancamento = Lancamento.builder()
                .descricao(dto.descricao())
                .valorOriginal(dto.valorOriginal())
                .taxaJuros(dto.taxaJuros())
                .tipoJuros(dto.tipoJuros())
                .juros(dto.juros())
                .desconto(dto.desconto())
                .valor(calcularValor(dto.valorOriginal(), dto.juros(), dto.desconto()))
                .tipo(dto.tipo())
                .formaPagamento(dto.formaPagamento())
                .cartaoCreditoId(dto.cartaoCreditoId())
                .numeroParcelas(dto.numeroParcelas() != null ? dto.numeroParcelas() : 1)
                .dataLancamento(dto.dataLancamento() != null ? dto.dataLancamento() : LocalDate.now())
                .dataVencimento(dto.dataVencimento())
                .observacao(dto.observacao())
                .centroCustoId(dto.centroCustoId())
                .usuarioId(email)
                .build();

        return LancamentoResponseDTO.fromEntity(lancamentoRepository.save(lancamento));
    }

    @Transactional
    public List<LancamentoResponseDTO> criarParcelado(LancamentoParceladoRequestDTO dto) {
        String email = getEmailAutenticado();

        validarCartaoCredito(dto.formaPagamento(), dto.cartaoCreditoId(), email);
        validarCentroCusto(dto.centroCustoId());

        String grupoParcelaId = UUID.randomUUID().toString();
        LocalDate dataInicio = dto.dataInicio() != null ? dto.dataInicio() : LocalDate.now();

        BigDecimal valorParcela = dto.valorTotal()
                .divide(BigDecimal.valueOf(dto.numeroParcelas()), 2, RoundingMode.DOWN);
        BigDecimal resto = dto.valorTotal()
                .subtract(valorParcela.multiply(BigDecimal.valueOf(dto.numeroParcelas())));

        List<Lancamento> lancamentos = new ArrayList<>();

        for (int i = 1; i <= dto.numeroParcelas(); i++) {
            BigDecimal valorAtual = (i == dto.numeroParcelas())
                    ? valorParcela.add(resto)
                    : valorParcela;

            Lancamento lancamento = Lancamento.builder()
                    .descricao(dto.descricao() + " (" + i + "/" + dto.numeroParcelas() + ")")
                    .valorOriginal(valorAtual)
                    .valor(valorAtual)
                    .tipo(TipoLancamento.DESPESA)
                    .formaPagamento(dto.formaPagamento())
                    .cartaoCreditoId(dto.cartaoCreditoId())
                    .numeroParcelas(dto.numeroParcelas())
                    .parcelaNumero(i)
                    .grupoParcelaId(grupoParcelaId)
                    .dataLancamento(LocalDate.now())
                    .dataVencimento(dataInicio.plusMonths(i - 1))
                    .observacao(dto.observacao())
                    .centroCustoId(dto.centroCustoId())
                    .usuarioId(email)
                    .build();

            lancamentos.add(lancamentoRepository.save(lancamento));
        }

        return lancamentos.stream().map(LancamentoResponseDTO::fromEntity).toList();
    }

    public List<LancamentoResponseDTO> listar() {
        String email = getEmailAutenticado();
        return lancamentoRepository.findByUsuarioIdAndAtivoTrue(email)
                .stream()
                .map(LancamentoResponseDTO::fromEntity)
                .toList();
    }

    public List<LancamentoResponseDTO> listarPorTipo(TipoLancamento tipo) {
        String email = getEmailAutenticado();
        return lancamentoRepository.findByUsuarioIdAndTipoAndAtivoTrue(email, tipo)
                .stream()
                .map(LancamentoResponseDTO::fromEntity)
                .toList();
    }

    public List<LancamentoResponseDTO> listarPorStatus(StatusLancamento status) {
        String email = getEmailAutenticado();
        return lancamentoRepository.findByUsuarioIdAndStatusAndAtivoTrue(email, status)
                .stream()
                .map(LancamentoResponseDTO::fromEntity)
                .toList();
    }

    public List<LancamentoResponseDTO> listarPorDescricao(String descricao) {
        String email = getEmailAutenticado();
        return lancamentoRepository.findByUsuarioIdAndDescricaoContainingIgnoreCaseAndAtivoTrue(email, descricao)
                .stream()
                .map(LancamentoResponseDTO::fromEntity)
                .toList();
    }

    public LancamentoResponseDTO buscarPorId(Long id) {
        String email = getEmailAutenticado();
        Lancamento lancamento = lancamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LancamentoNaoAutorizadoException("Lançamento não encontrado"));

        if (!lancamento.getUsuarioId().equals(email)) {
            throw new LancamentoNaoAutorizadoException("Acesso negado");
        }

        return LancamentoResponseDTO.fromEntity(lancamento);
    }

    public LancamentoResponseDTO atualizar(Long id, LancamentoRequestDTO dto) {
        String email = getEmailAutenticado();
        Lancamento lancamento = lancamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Lançamento não encontrado"));

        if (!lancamento.getUsuarioId().equals(email)) {
            throw new LancamentoNaoAutorizadoException("Acesso negado");
        }

        validarCentroCusto(dto.centroCustoId());
        validarCartaoCredito(dto.formaPagamento(), dto.cartaoCreditoId(), email);

        lancamento.setDescricao(dto.descricao());
        lancamento.setValorOriginal(dto.valorOriginal());
        lancamento.setTaxaJuros(dto.taxaJuros());
        lancamento.setTipoJuros(dto.tipoJuros());
        lancamento.setFormaPagamento(dto.formaPagamento());
        lancamento.setCartaoCreditoId(dto.cartaoCreditoId());
        lancamento.setNumeroParcelas(dto.numeroParcelas() != null ? dto.numeroParcelas() : 1);
        lancamento.setJuros(dto.juros());
        lancamento.setDesconto(dto.desconto());
        lancamento.setValor(calcularValor(dto.valorOriginal(), dto.juros(), dto.desconto()));
        lancamento.setTipo(dto.tipo());
        lancamento.setDataLancamento(dto.dataLancamento() != null ? dto.dataLancamento() : lancamento.getDataLancamento());
        lancamento.setDataVencimento(dto.dataVencimento());
        lancamento.setObservacao(dto.observacao());
        lancamento.setCentroCustoId(dto.centroCustoId());

        return LancamentoResponseDTO.fromEntity(lancamentoRepository.save(lancamento));
    }

    public void deletar(Long id) {
        String email = getEmailAutenticado();
        Lancamento lancamento = lancamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Lançamento não encontrado"));

        if (!lancamento.getUsuarioId().equals(email)) {
            throw new LancamentoNaoAutorizadoException("Acesso negado");
        }

        if (lancamento.getStatus() == StatusLancamento.EFETIVADO) {
            throw new LancamentoNaoAutorizadoException(
                    "Lançamento já efetivado não pode ser excluído, pois geraria um movimento bancário " +
                    "órfão na conta. Cancele o lançamento em vez de excluir.");
        }

        lancamento.setAtivo(false);
        lancamentoRepository.save(lancamento);
    }

    /** Exclui de uma vez todas as parcelas do grupo (mesma compra parcelada). Parcelas já
     *  efetivadas são preservadas — mesma regra do {@link #deletar}, aplicada individualmente
     *  a cada parcela, pra não deixar movimento bancário órfão. */
    @Transactional
    public void deletarGrupo(String grupoParcelaId) {
        String email = getEmailAutenticado();
        List<Lancamento> parcelas = lancamentoRepository
                .findByGrupoParcelaIdAndUsuarioIdAndAtivoTrue(grupoParcelaId, email);

        if (parcelas.isEmpty()) {
            throw new LancamentoNaoEncontradoException("Grupo de parcelas não encontrado");
        }

        for (Lancamento parcela : parcelas) {
            if (parcela.getStatus() != StatusLancamento.EFETIVADO) {
                parcela.setAtivo(false);
            }
        }
        lancamentoRepository.saveAll(parcelas);
    }

    /** Registra o lançamento bancário na conta ANTES de marcar o título como EFETIVADO: se a chamada a
     *  ms-contas falhar (conta não encontrada, saldo insuficiente, serviço fora do ar), a exceção é
     *  propagada e nada é persistido aqui — o título continua PENDENTE, sem estado parcial (título
     *  efetivado sem o saldo da conta ter sido atualizado). */
    public LancamentoResponseDTO efetivar(Long id, EfetivarRequestDTO dto) {
        String email = getEmailAutenticado();
        Lancamento lancamento = lancamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Lançamento não encontrado"));

        if (!lancamento.getUsuarioId().equals(email)) {
            throw new LancamentoNaoAutorizadoException("Acesso negado");
        }

        if (lancamento.getStatus() == StatusLancamento.CANCELADO) {
            throw new LancamentoNaoAutorizadoException("Lançamento cancelado não pode ser efetivado");
        }

        LocalDate dataPagamento = dto.dataPagamento() != null ? dto.dataPagamento() : LocalDate.now();

        try {
            contaClient.registrarLancamento(dto.contaId(),
                    new LancamentoBancarioRequestDTO(
                            lancamento.getTipo() == TipoLancamento.RECEITA
                                    ? TipoMovimentoBancario.ENTRADA : TipoMovimentoBancario.SAIDA,
                            lancamento.getValor(),
                            dataPagamento,
                            lancamento.getDescricao(),
                            lancamento.getId()
                    ), getToken());
        } catch (FeignException.NotFound e) {
            throw new LancamentoNaoEncontradoException("Conta não encontrada");
        } catch (FeignException e) {
            throw new LancamentoNaoAutorizadoException(
                    "Não foi possível lançar na conta selecionada (saldo insuficiente ou erro na conta). " +
                    "O título não foi efetivado.");
        }

        lancamento.setContaId(dto.contaId());
        lancamento.setStatus(StatusLancamento.EFETIVADO);
        lancamento.setDataPagamento(dataPagamento);
        return LancamentoResponseDTO.fromEntity(lancamentoRepository.save(lancamento));
    }

    public List<LancamentoVencimentoDTO> buscarVencendoHoje() {
        return lancamentoRepository
                .findByDataVencimentoAndStatusAndAtivoTrue(LocalDate.now(), StatusLancamento.PENDENTE)
                .stream()
                .map(l -> new LancamentoVencimentoDTO(
                        l.getId(),
                        l.getDescricao(),
                        l.getValor(),
                        l.getDataVencimento(),
                        l.getUsuarioId()
                ))
                .toList();
    }

    /** Apaga de vez todos os lançamentos e cartões de crédito do usuário (exclusão de conta em cascata).
     *  Lançamentos primeiro: cartao_credito_id em lancamentos tem FK pra cartao_credito, então apagar
     *  os cartões antes quebraria a integridade referencial se sobrasse algum lançamento. */
    @Transactional
    public void excluirDadosUsuario(String usuarioId) {
        lancamentoRepository.deleteByUsuarioId(usuarioId);
        cartaoCreditoRepository.deleteByUsuarioId(usuarioId);
    }
}