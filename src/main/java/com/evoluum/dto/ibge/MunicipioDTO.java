package com.evoluum.dto.ibge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MunicipioDTO {

    private Long id;
    private String nome;
    private MicrorregiaoDTO microrregiao;
    @JsonProperty("regiao-imediata")
    private RegiaoImediataDTO regiaoImediata;
}
