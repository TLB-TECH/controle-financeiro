package br.com.controlefinanceiro.mslancamentos.controller;

import br.com.controlefinanceiro.mslancamentos.dto.LancamentoRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.LancamentoResponseDTO;
import br.com.controlefinanceiro.mslancamentos.enums.StatusLancamento;
import br.com.controlefinanceiro.mslancamentos.enums.TipoLancamento;
import br.com.controlefinanceiro.mslancamentos.service.LancamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity<LancamentoResponseDTO> criar(@RequestBody @Valid LancamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<LancamentoResponseDTO>> listar(
            @RequestParam(required = false) TipoLancamento tipo,
            @RequestParam(required = false) StatusLancamento status) {

        if (tipo != null) {
            return ResponseEntity.ok(lancamentoService.listarPorTipo(tipo));
        }
        if (status != null) {
            return ResponseEntity.ok(lancamentoService.listarPorStatus(status));
        }
        return ResponseEntity.ok(lancamentoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(lancamentoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoResponseDTO> atualizar(@PathVariable Long id,
                                                           @RequestBody @Valid LancamentoRequestDTO dto) {
        return ResponseEntity.ok(lancamentoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        lancamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/efetivar")
    public ResponseEntity<LancamentoResponseDTO> efetivar(@PathVariable Long id) {
        return ResponseEntity.ok(lancamentoService.efetivar(id));
    }
}
