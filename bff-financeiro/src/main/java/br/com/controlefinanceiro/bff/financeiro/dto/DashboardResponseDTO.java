package br.com.controlefinanceiro.bff.financeiro.dto;

import java.util.List;

public record DashboardResponseDTO(
        FluxoCaixaResponseDTO fluxoCaixa,
        List<LancamentoResponseDTO> lancamentos,
        List<CentroCustoResponseDTO> centrosCusto
) {}
