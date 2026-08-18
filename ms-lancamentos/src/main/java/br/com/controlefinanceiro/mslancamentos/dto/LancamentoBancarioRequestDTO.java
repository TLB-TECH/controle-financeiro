package br.com.controlefinanceiro.mslancamentos.dto;

import br.com.controlefinanceiro.mslancamentos.enums.TipoMovimentoBancario;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Espelha o LancamentoBancarioRequestDTO de ms-contas — payload enviado via ContaClient
 *  ao efetivar um título, para registrar o movimento bancário correspondente na conta. */
public record LancamentoBancarioRequestDTO(
        TipoMovimentoBancario tipo,
        BigDecimal valor,
        LocalDate data,
        String descricao,
        Long tituloId
) {}
