package br.com.controlefinanceiro.mslancamentos.service;

import br.com.controlefinanceiro.mslancamentos.dto.EfetivarRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoResponseDTO;
import br.com.controlefinanceiro.mslancamentos.entity.Lancamento;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import br.com.controlefinanceiro.mslancamentos.exception.LancamentoNaoAutorizadoException;
import br.com.controlefinanceiro.mslancamentos.exception.LancamentoNaoEncontradoException;
import br.com.controlefinanceiro.mslancamentos.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public LancamentoResponseDTO criar(LancamentoRequestDTO dto) {
        String email = getEmailAutenticado();

        Lancamento lancamento = Lancamento.builder()
                .descricao(dto.descricao())
                .valor(dto.valor())
                .tipo(dto.tipo())
                .dataLancamento(dto.dataLancamento() != null ? dto.dataLancamento() : LocalDate.now())
                .dataVencimento(dto.dataVencimento())
                .observacao(dto.observacao())
                .usuarioId(email)
                .build();

        return LancamentoResponseDTO.fromEntity(lancamentoRepository.save(lancamento));
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

    public LancamentoResponseDTO buscarPorId(Long id) {
        String email = getEmailAutenticado();
        Lancamento lancamento = lancamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() ->  new LancamentoNaoAutorizadoException("Lançamento não encontrado"));

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

        lancamento.setDescricao(dto.descricao());
        lancamento.setValor(dto.valor());
        lancamento.setTipo(dto.tipo());
        lancamento.setDataLancamento(dto.dataLancamento() != null ? dto.dataLancamento() : lancamento.getDataLancamento());
        lancamento.setDataVencimento(dto.dataVencimento());
        lancamento.setObservacao(dto.observacao());

        return LancamentoResponseDTO.fromEntity(lancamentoRepository.save(lancamento));
    }

    public void deletar(Long id) {
        String email = getEmailAutenticado();
        Lancamento lancamento = lancamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LancamentoNaoEncontradoException("Lançamento não encontrado"));

        if (!lancamento.getUsuarioId().equals(email)) {
            throw new LancamentoNaoAutorizadoException("Acesso negado");
        }

        lancamento.setAtivo(false);
        lancamentoRepository.save(lancamento);
    }

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

        lancamento.setStatus(StatusLancamento.EFETIVADO);
        lancamento.setDataPagamento(dto.dataPagamento() != null ? dto.dataPagamento() : LocalDate.now());
        return LancamentoResponseDTO.fromEntity(lancamentoRepository.save(lancamento));
    }
}
