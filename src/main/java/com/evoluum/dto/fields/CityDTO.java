package com.evoluum.dto.fields;

import com.evoluum.dto.ibge.MesorregiaoDTO;
import com.evoluum.dto.ibge.MicrorregiaoDTO;
import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.RegiaoDTO;
import com.evoluum.dto.ibge.UfDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que será retornado no CSV e JSON
 */
@NoArgsConstructor
@Data
public class CityDTO {

    private Long idEstado;
    private String siglaEstado;
    private String regiaoNome;
    private String nomeCidade;
    private String nomeMesorregiao;
    private String nomeFormatado;

    public CityDTO(MunicipioDTO municipioDTO) {
        final MicrorregiaoDTO microrregiao = municipioDTO.getMicrorregiao() != null ? municipioDTO.getMicrorregiao() : new MicrorregiaoDTO();
        final MesorregiaoDTO mesorregiao = microrregiao.getMesorregiao() != null ? microrregiao.getMesorregiao() : new MesorregiaoDTO();
        final UfDTO uf = mesorregiao.getUf() != null ? mesorregiao.getUf() : new UfDTO();
        final RegiaoDTO regiao = uf.getRegiao() != null ? uf.getRegiao() : new RegiaoDTO();

        this.nomeCidade = municipioDTO.getNome();
        this.nomeMesorregiao = mesorregiao.getNome();
        this.idEstado = uf.getId();
        this.siglaEstado = uf.getSigla();
        this.regiaoNome = regiao.getNome();
        this.nomeFormatado = this.nomeCidade + "/" + this.siglaEstado;
    }

    public String getNomeFormatado() {
        return this.nomeCidade + "/" + this.siglaEstado;
    }

}
