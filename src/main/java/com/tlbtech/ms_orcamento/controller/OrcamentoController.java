package com.tlbtech.ms_orcamento.controller;

import com.tlbtech.ms_orcamento.dto.OrcamentoRequestDTO;
import com.tlbtech.ms_orcamento.dto.OrcamentoResponseDTO;
import com.tlbtech.ms_orcamento.service.OrcamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/mes/{mes}/ano/{ano}")
    public ResponseEntity<List<OrcamentoResponseDTO>> listarPorMesAno(
            @PathVariable Integer mes,
            @PathVariable Integer ano) {
        return ResponseEntity.ok(service.listarPorMesAno(mes, ano));
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> criar(@Valid @RequestBody OrcamentoRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrcamentoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
