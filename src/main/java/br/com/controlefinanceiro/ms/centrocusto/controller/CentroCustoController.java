package br.com.controlefinanceiro.ms.centrocusto.controller;

import br.com.controlefinanceiro.ms.centrocusto.dto.CentroCustoRequestDTO;
import br.com.controlefinanceiro.ms.centrocusto.dto.CentroCustoResponseDTO;
import br.com.controlefinanceiro.ms.centrocusto.service.CentroCustoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/centros-custo")
@RequiredArgsConstructor
public class CentroCustoController {

    private final CentroCustoService centroCustoService;

    @PostMapping
    public ResponseEntity<CentroCustoResponseDTO> criar(@RequestBody @Valid CentroCustoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(centroCustoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CentroCustoResponseDTO>> listar(
            @RequestParam(required = false) Boolean ativo) {
        return ResponseEntity.ok(centroCustoService.listar(ativo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroCustoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(centroCustoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CentroCustoResponseDTO> atualizar(@PathVariable Long id,
                                                            @RequestBody @Valid CentroCustoRequestDTO dto) {
        return ResponseEntity.ok(centroCustoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        centroCustoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<CentroCustoResponseDTO> reativar(@PathVariable Long id) {
        return ResponseEntity.ok(centroCustoService.reativar(id));
    }
}