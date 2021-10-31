package com.evoluum.dto.ibge;

import lombok.Data;

@Data
public class UfDTO {

    private Long id;
    private String sigla;
    private String nome;
    private RegiaoDTO regiao;
}
