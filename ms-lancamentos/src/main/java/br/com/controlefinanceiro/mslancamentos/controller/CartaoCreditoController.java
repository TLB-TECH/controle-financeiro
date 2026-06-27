package br.com.controlefinanceiro.mslancamentos.controller;

import br.com.controlefinanceiro.mslancamentos.dto.CartaoCreditoRequestDTO;
import br.com.controlefinanceiro.mslancamentos.dto.CartaoCreditoResponseDTO;
import br.com.controlefinanceiro.mslancamentos.service.CartaoCreditoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoCreditoController {

    private final CartaoCreditoService service;

    @PostMapping
    public ResponseEntity<CartaoCreditoResponseDTO> criar(@RequestBody @Valid CartaoCreditoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CartaoCreditoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoCreditoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoCreditoResponseDTO> atualizar(@PathVariable Long id,
                                                              @RequestBody @Valid CartaoCreditoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}