package com.tlbtech.ms_orcamento.controller;

import com.tlbtech.ms_orcamento.dto.MetaAnualRequestDTO;
import com.tlbtech.ms_orcamento.dto.MetaAnualResponseDTO;
import com.tlbtech.ms_orcamento.dto.MetaMensalItemDTO;
import com.tlbtech.ms_orcamento.dto.MetaMensalResponseDTO;
import com.tlbtech.ms_orcamento.service.MetaAplicacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas-aplicacao")
public class MetaAplicacaoController {

    private final MetaAplicacaoService service;

    public MetaAplicacaoController(MetaAplicacaoService service) {
        this.service = service;
    }

    @GetMapping("/mensal/{ano}")
    public ResponseEntity<List<MetaMensalResponseDTO>> listarMensal(@PathVariable Integer ano) {
        return ResponseEntity.ok(service.listarMensal(ano));
    }

    @PutMapping("/mensal/{ano}")
    public ResponseEntity<List<MetaMensalResponseDTO>> salvarMensal(
            @PathVariable Integer ano,
            @Valid @RequestBody List<MetaMensalItemDTO> itens) {
        return ResponseEntity.ok(service.salvarMensal(ano, itens));
    }

    @GetMapping("/anual/{ano}")
    public ResponseEntity<MetaAnualResponseDTO> obterAnual(@PathVariable Integer ano) {
        return ResponseEntity.ok(service.obterAnual(ano));
    }

    @PutMapping("/anual/{ano}")
    public ResponseEntity<MetaAnualResponseDTO> salvarAnual(
            @PathVariable Integer ano,
            @Valid @RequestBody MetaAnualRequestDTO dto) {
        return ResponseEntity.ok(service.salvarAnual(ano, dto));
    }
}
