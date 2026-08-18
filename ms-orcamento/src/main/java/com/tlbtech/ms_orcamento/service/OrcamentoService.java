package com.tlbtech.ms_orcamento.service;

import com.tlbtech.ms_orcamento.dto.OrcamentoRequestDTO;
import com.tlbtech.ms_orcamento.dto.OrcamentoResponseDTO;
import com.tlbtech.ms_orcamento.model.Orcamento;
import com.tlbtech.ms_orcamento.model.TipoAlvoOrcamento;
import com.tlbtech.ms_orcamento.model.TipoOrcamento;
import com.tlbtech.ms_orcamento.repository.OrcamentoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrcamentoService {

    private final OrcamentoRepository repository;

    public OrcamentoService(OrcamentoRepository repository) {
        this.repository = repository;
    }

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<OrcamentoResponseDTO> listar() {
        return repository.findByUsuarioIdAndAtivoTrue(getEmailAutenticado())
                .stream()
                .map(OrcamentoResponseDTO::fromEntity)
                .toList();
    }

    /** Um orçamento por alvo (centro de custo, cartão específico ou "todos os cartões"): quando mais de
     *  um tipo está vigente no período para o mesmo alvo, só o mais específico prevalece —
     *  MENSAL > ANUAL > DEFINITIVO — em vez de somar/exibir todos. */
    public List<OrcamentoResponseDTO> listarPorMesAno(Integer mes, Integer ano) {
        String email = getEmailAutenticado();
        Map<String, Orcamento> maisEspecificoPorAlvo = new LinkedHashMap<>();
        for (Orcamento o : repository.findVigentesPorMesAno(email, mes, ano)) {
            String chave = chaveAlvo(o);
            Orcamento atual = maisEspecificoPorAlvo.get(chave);
            if (atual == null || prioridade(o.getTipo()) < prioridade(atual.getTipo())) {
                maisEspecificoPorAlvo.put(chave, o);
            }
        }
        return maisEspecificoPorAlvo.values()
                .stream()
                .map(OrcamentoResponseDTO::fromEntity)
                .toList();
    }

    private String chaveAlvo(Orcamento o) {
        return switch (o.getTipoAlvo()) {
            case CENTRO_CUSTO -> "CC:" + o.getCentroCustoId();
            case CARTAO -> "CARTAO:" + o.getCartaoCreditoId();
            case TODOS_CARTOES -> "TODOS_CARTOES";
        };
    }

    private int prioridade(TipoOrcamento tipo) {
        return switch (tipo) {
            case MENSAL -> 0;
            case ANUAL -> 1;
            case DEFINITIVO -> 2;
        };
    }

    public OrcamentoResponseDTO buscarPorId(Long id) {
        Orcamento o = repository.findByIdAndUsuarioId(id, getEmailAutenticado())
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        return OrcamentoResponseDTO.fromEntity(o);
    }

    public OrcamentoResponseDTO criar(OrcamentoRequestDTO dto) {
        String email = getEmailAutenticado();
        validarAlvo(dto);
        validarPeriodo(dto);
        verificarDuplicidade(email, dto, null);

        Orcamento o = new Orcamento();
        o.setUsuarioId(email);
        preencher(o, dto);

        return OrcamentoResponseDTO.fromEntity(repository.save(o));
    }

    public OrcamentoResponseDTO atualizar(Long id, OrcamentoRequestDTO dto) {
        String email = getEmailAutenticado();
        validarAlvo(dto);
        validarPeriodo(dto);
        verificarDuplicidade(email, dto, id);

        Orcamento o = repository.findByIdAndUsuarioId(id, email)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        preencher(o, dto);

        return OrcamentoResponseDTO.fromEntity(repository.save(o));
    }

    private void preencher(Orcamento o, OrcamentoRequestDTO dto) {
        o.setTipoAlvo(dto.tipoAlvo());
        o.setCentroCustoId(dto.tipoAlvo() == TipoAlvoOrcamento.CENTRO_CUSTO ? dto.centroCustoId() : null);
        o.setCartaoCreditoId(dto.tipoAlvo() == TipoAlvoOrcamento.CARTAO ? dto.cartaoCreditoId() : null);
        o.setTipo(dto.tipo());
        o.setMes(dto.tipo() == TipoOrcamento.MENSAL ? dto.mes() : null);
        o.setAno(dto.tipo() != TipoOrcamento.DEFINITIVO ? dto.ano() : null);
        o.setValorLimite(dto.valorLimite());
    }

    public void excluir(Long id) {
        Orcamento o = repository.findByIdAndUsuarioId(id, getEmailAutenticado())
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        o.setAtivo(false);
        repository.save(o);
    }

    /** Apaga de vez todos os orçamentos do usuário (exclusão de conta em cascata). */
    @Transactional
    public void excluirDadosUsuario(String usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
    }

    private void validarAlvo(OrcamentoRequestDTO dto) {
        switch (dto.tipoAlvo()) {
            case CENTRO_CUSTO -> {
                if (dto.centroCustoId() == null) {
                    throw new RuntimeException("Centro de custo obrigatório");
                }
            }
            case CARTAO -> {
                if (dto.cartaoCreditoId() == null) {
                    throw new RuntimeException("Cartão de crédito obrigatório");
                }
            }
            case TODOS_CARTOES -> {
                // sem alvo específico — aplica-se à soma de todos os cartões
            }
        }
    }

    private void validarPeriodo(OrcamentoRequestDTO dto) {
        if (dto.tipo() == TipoOrcamento.MENSAL && (dto.mes() == null || dto.ano() == null)) {
            throw new RuntimeException("Mês e ano são obrigatórios para orçamento mensal");
        }
        if (dto.tipo() == TipoOrcamento.ANUAL && dto.ano() == null) {
            throw new RuntimeException("Ano é obrigatório para orçamento anual");
        }
    }

    private void verificarDuplicidade(String email, OrcamentoRequestDTO dto, Long idAtual) {
        boolean existe = switch (dto.tipoAlvo()) {
            case CENTRO_CUSTO -> existeDuplicidadeCentroCusto(email, dto, idAtual);
            case CARTAO -> existeDuplicidadeCartao(email, dto, idAtual);
            case TODOS_CARTOES -> existeDuplicidadeTodosCartoes(email, dto, idAtual);
        };
        if (existe) {
            throw new RuntimeException(mensagemDuplicidade(dto.tipoAlvo(), dto.tipo()));
        }
    }

    private String mensagemDuplicidade(TipoAlvoOrcamento alvo, TipoOrcamento tipo) {
        String periodo = switch (tipo) {
            case MENSAL -> "neste mês/ano";
            case ANUAL -> "neste ano";
            case DEFINITIVO -> "";
        };
        String sujeito = switch (alvo) {
            case CENTRO_CUSTO -> "esse centro de custo";
            case CARTAO -> "esse cartão";
            case TODOS_CARTOES -> "\"Todos os Cartões\"";
        };
        return ("Já existe orçamento " + (tipo == TipoOrcamento.DEFINITIVO ? "definitivo " : "")
                + "para " + sujeito + " " + periodo).trim();
    }

    private boolean existeDuplicidadeCentroCusto(String email, OrcamentoRequestDTO dto, Long idAtual) {
        return switch (dto.tipo()) {
            case MENSAL -> repository.findByUsuarioIdAndCentroCustoIdAndMesAndAnoAndTipo(
                            email, dto.centroCustoId(), dto.mes(), dto.ano(), TipoOrcamento.MENSAL)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
            case ANUAL -> repository.findByUsuarioIdAndCentroCustoIdAndAnoAndTipo(
                            email, dto.centroCustoId(), dto.ano(), TipoOrcamento.ANUAL)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
            case DEFINITIVO -> repository.findByUsuarioIdAndCentroCustoIdAndTipoAndAtivoTrue(
                            email, dto.centroCustoId(), TipoOrcamento.DEFINITIVO)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
        };
    }

    private boolean existeDuplicidadeCartao(String email, OrcamentoRequestDTO dto, Long idAtual) {
        return switch (dto.tipo()) {
            case MENSAL -> repository.findByUsuarioIdAndCartaoCreditoIdAndMesAndAnoAndTipo(
                            email, dto.cartaoCreditoId(), dto.mes(), dto.ano(), TipoOrcamento.MENSAL)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
            case ANUAL -> repository.findByUsuarioIdAndCartaoCreditoIdAndAnoAndTipo(
                            email, dto.cartaoCreditoId(), dto.ano(), TipoOrcamento.ANUAL)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
            case DEFINITIVO -> repository.findByUsuarioIdAndCartaoCreditoIdAndTipoAndAtivoTrue(
                            email, dto.cartaoCreditoId(), TipoOrcamento.DEFINITIVO)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
        };
    }

    private boolean existeDuplicidadeTodosCartoes(String email, OrcamentoRequestDTO dto, Long idAtual) {
        return switch (dto.tipo()) {
            case MENSAL -> repository.findByUsuarioIdAndTipoAlvoAndMesAndAnoAndTipo(
                            email, TipoAlvoOrcamento.TODOS_CARTOES, dto.mes(), dto.ano(), TipoOrcamento.MENSAL)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
            case ANUAL -> repository.findByUsuarioIdAndTipoAlvoAndAnoAndTipo(
                            email, TipoAlvoOrcamento.TODOS_CARTOES, dto.ano(), TipoOrcamento.ANUAL)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
            case DEFINITIVO -> repository.findByUsuarioIdAndTipoAlvoAndTipoAndAtivoTrue(
                            email, TipoAlvoOrcamento.TODOS_CARTOES, TipoOrcamento.DEFINITIVO)
                    .filter(o -> !o.getId().equals(idAtual)).isPresent();
        };
    }
}
