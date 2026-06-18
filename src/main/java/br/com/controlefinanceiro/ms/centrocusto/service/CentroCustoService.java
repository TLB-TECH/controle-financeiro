package br.com.controlefinanceiro.ms.centrocusto.service;

import br.com.controlefinanceiro.ms.centrocusto.dto.CentroCustoRequestDTO;
import br.com.controlefinanceiro.ms.centrocusto.dto.CentroCustoResponseDTO;
import br.com.controlefinanceiro.ms.centrocusto.model.CentroCusto;
import br.com.controlefinanceiro.ms.centrocusto.repository.CentroCustoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CentroCustoService {

    private final CentroCustoRepository centroCustoRepository;

    public CentroCustoResponseDTO criar(CentroCustoRequestDTO dto) {
        String email = getEmailAutenticado();

        if (centroCustoRepository.existsByNomeAndUsuarioIdAndAtivoTrue(dto.nome(), email)) {
            throw new IllegalArgumentException("Já existe um centro de custo com esse nome.");
        }

        CentroCusto centroCusto = CentroCusto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .usuarioId(email)
                .build();

        return CentroCustoResponseDTO.fromEntity(centroCustoRepository.save(centroCusto));
    }

    public List<CentroCustoResponseDTO> listar() {
        String email = getEmailAutenticado();
        return centroCustoRepository.findByUsuarioIdAndAtivoTrue(email)
                .stream()
                .map(CentroCustoResponseDTO::fromEntity)
                .toList();
    }

    public CentroCustoResponseDTO buscarPorId(Long id) {
        String email = getEmailAutenticado();
        CentroCusto centroCusto = centroCustoRepository
                .findByIdAndUsuarioIdAndAtivoTrue(id, email)
                .orElseThrow(() -> new RuntimeException("Centro de custo não encontrado."));
        return CentroCustoResponseDTO.fromEntity(centroCusto);
    }

    public CentroCustoResponseDTO atualizar(Long id, CentroCustoRequestDTO dto) {
        String email = getEmailAutenticado();
        CentroCusto centroCusto = centroCustoRepository
                .findByIdAndUsuarioIdAndAtivoTrue(id, email)
                .orElseThrow(() -> new RuntimeException("Centro de custo não encontrado."));

        centroCusto.setNome(dto.nome());
        centroCusto.setDescricao(dto.descricao());

        return CentroCustoResponseDTO.fromEntity(centroCustoRepository.save(centroCusto));
    }

    public void deletar(Long id) {
        String email = getEmailAutenticado();
        CentroCusto centroCusto = centroCustoRepository
                .findByIdAndUsuarioIdAndAtivoTrue(id, email)
                .orElseThrow(() -> new RuntimeException("Centro de custo não encontrado."));

        centroCusto.setAtivo(false);
        centroCustoRepository.save(centroCusto);
    }

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}