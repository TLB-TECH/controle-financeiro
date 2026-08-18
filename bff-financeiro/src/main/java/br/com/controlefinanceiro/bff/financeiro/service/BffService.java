package br.com.controlefinanceiro.bff.financeiro.service;

import br.com.controlefinanceiro.bff.financeiro.client.CentroCustoClient;
import br.com.controlefinanceiro.bff.financeiro.client.FluxoCaixaClient;
import br.com.controlefinanceiro.bff.financeiro.client.LancamentosClient;
import br.com.controlefinanceiro.bff.financeiro.dto.CentroCustoResponseDTO;
import br.com.controlefinanceiro.bff.financeiro.dto.DashboardResponseDTO;
import br.com.controlefinanceiro.bff.financeiro.dto.FluxoCaixaResponseDTO;
import br.com.controlefinanceiro.bff.financeiro.dto.LancamentoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BffService {

    private final LancamentosClient lancamentosClient;
    private final CentroCustoClient centroCustoClient;
    private final FluxoCaixaClient fluxoCaixaClient;

    public DashboardResponseDTO getDashboard(String token) {
        FluxoCaixaResponseDTO fluxoCaixa = fluxoCaixaClient.calcular(token);
        List<LancamentoResponseDTO> lancamentos = lancamentosClient.listarLancamentos(token);
        List<CentroCustoResponseDTO> centrosCusto = centroCustoClient.listarCentrosCusto(token);

        return new DashboardResponseDTO(fluxoCaixa, lancamentos, centrosCusto);
    }
}
