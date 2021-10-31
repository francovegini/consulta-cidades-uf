package com.evoluum.dto.ibge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegiaoImediataDTO {

    private Long id;
    private String nome;
    @JsonProperty("regiao-intermediaria")
    private RegiaoIntermediariaDTO regiaoIntermediaria;
}
