package br.com.controlefinanceiro.msusuarios.dto;

public record AcessoDTO(
        boolean temAcesso,
        String status,
        Long diasRestantesTrial
) {}
