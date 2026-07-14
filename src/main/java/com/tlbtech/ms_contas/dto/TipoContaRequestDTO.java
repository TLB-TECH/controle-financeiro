package com.tlbtech.ms_contas.dto;

import com.tlbtech.ms_contas.model.CategoriaTipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TipoContaRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "Categoria é obrigatória")
        CategoriaTipoConta categoria
) {}
