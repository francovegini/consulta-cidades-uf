package com.evoluum.dto.ibge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegiaoIntermediariaDTO {

    private Long id;
    private String nome;
    @JsonProperty("UF")
    private UfDTO uf;
}
