package com.evoluum.dto.ibge;

import lombok.Data;

@Data
public class MicrorregiaoDTO {

    private Long id;
    private String nome;
    private MesorregiaoDTO mesorregiao;
}
