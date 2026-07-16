package com.tlbtech.ms_orcamento.service;

import com.tlbtech.ms_orcamento.dto.OrcamentoRequestDTO;
import com.tlbtech.ms_orcamento.dto.OrcamentoResponseDTO;
import com.tlbtech.ms_orcamento.model.Orcamento;
import com.tlbtech.ms_orcamento.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrcamentoService {

    private final OrcamentoRepository repository;

    public OrcamentoService(OrcamentoRepository repository) {
        this.repository = repository;
    }

    public List<OrcamentoResponseDTO> listar() {
        return repository.findByAtivoTrue()
                .stream()
                .map(OrcamentoResponseDTO::fromEntity)
                .toList();
    }

    public List<OrcamentoResponseDTO> listarPorMesAno(Integer mes, Integer ano) {
        return repository.findByMesAndAnoAndAtivoTrue(mes, ano)
                .stream()
                .map(OrcamentoResponseDTO::fromEntity)
                .toList();
    }

    public OrcamentoResponseDTO buscarPorId(Long id) {
        Orcamento o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        return OrcamentoResponseDTO.fromEntity(o);
    }

    public OrcamentoResponseDTO criar(OrcamentoRequestDTO dto) {
        boolean existe = repository
                .findByCentroCustoIdAndMesAndAno(dto.centroCustoId(), dto.mes(), dto.ano())
                .isPresent();

        if (existe) {
            throw new RuntimeException("Já existe orçamento para esse centro de custo neste mês/ano");
        }

        Orcamento o = new Orcamento();
        o.setCentroCustoId(dto.centroCustoId());
        o.setMes(dto.mes());
        o.setAno(dto.ano());
        o.setValorLimite(dto.valorLimite());

        return OrcamentoResponseDTO.fromEntity(repository.save(o));
    }

    public OrcamentoResponseDTO atualizar(Long id, OrcamentoRequestDTO dto) {
        Orcamento o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        o.setCentroCustoId(dto.centroCustoId());
        o.setMes(dto.mes());
        o.setAno(dto.ano());
        o.setValorLimite(dto.valorLimite());

        return OrcamentoResponseDTO.fromEntity(repository.save(o));
    }

    public void excluir(Long id) {
        Orcamento o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        o.setAtivo(false);
        repository.save(o);
    }
}
