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

    public List<CentroCustoResponseDTO> listar(Boolean ativo) {
        String email = getEmailAutenticado();

        List<CentroCusto> resultado;

        if (ativo == null) {
            resultado = centroCustoRepository.findByUsuarioId(email);
        } else if (ativo) {
            resultado = centroCustoRepository.findByUsuarioIdAndAtivoTrue(email);
        } else {
            resultado = centroCustoRepository.findByUsuarioIdAndAtivoFalse(email);
        }

        return resultado.stream()
                .sorted((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()))
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

    public void inativar(Long id) {
        String email = getEmailAutenticado();
        CentroCusto centroCusto = centroCustoRepository
                .findByIdAndUsuarioIdAndAtivoTrue(id, email)
                .orElseThrow(() -> new RuntimeException("Centro de custo não encontrado ou já inativo."));

        centroCusto.setAtivo(false);
        centroCustoRepository.save(centroCusto);
    }

    public CentroCustoResponseDTO reativar(Long id) {
        String email = getEmailAutenticado();
        CentroCusto centroCusto = centroCustoRepository
                .findByIdAndUsuarioId(id, email)
                .orElseThrow(() -> new RuntimeException("Centro de custo não encontrado."));

        if (centroCusto.getAtivo()) {
            throw new IllegalStateException("Centro de custo já está ativo.");
        }

        centroCusto.setAtivo(true);
        return CentroCustoResponseDTO.fromEntity(centroCustoRepository.save(centroCusto));
    }

    private String getEmailAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}