package br.com.controlefinanceiro.ms.fluxo.caixa.service;

import br.com.controlefinanceiro.ms.fluxo.caixa.client.LancamentoClient;
import br.com.controlefinanceiro.ms.fluxo.caixa.dto.FluxoCaixaResponseDTO;
import br.com.controlefinanceiro.ms.fluxo.caixa.dto.LancamentoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FluxoCaixaService {

    private final LancamentoClient lancamentoClient;

    public FluxoCaixaResponseDTO calcular(String token) {
        List<LancamentoResponseDTO> lancamentos = lancamentoClient.listarLancamentos(token);

        BigDecimal totalEntradas = lancamentos.stream()
                .filter(l -> "EFETIVADO".equals(l.status()) && "RECEITA".equals(l.tipo()))
                .map(LancamentoResponseDTO::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSaidas = lancamentos.stream()
                .filter(l -> "EFETIVADO".equals(l.status()) && "DESPESA".equals(l.tipo()))
                .map(LancamentoResponseDTO::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = totalEntradas.subtract(totalSaidas);

        return new FluxoCaixaResponseDTO(totalEntradas, totalSaidas, saldo);

    }
}
