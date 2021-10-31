package com.evoluum.dto;

import com.evoluum.dto.fields.CityDTO;
import com.evoluum.dto.ibge.MesorregiaoDTO;
import com.evoluum.dto.ibge.MicrorregiaoDTO;
import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.RegiaoDTO;
import com.evoluum.dto.ibge.RegiaoImediataDTO;
import com.evoluum.dto.ibge.UfDTO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CityDTOTest {

    @Test
    public void parseMunicipioToCity() {
        final RegiaoDTO regiao = new RegiaoDTO();
        regiao.setId(1L);
        regiao.setNome("Região Sul");
        regiao.setSigla("Sul");

        final UfDTO uf = new UfDTO();
        uf.setId(1L);
        uf.setNome("Santa Catarina");
        uf.setRegiao(regiao);
        uf.setSigla("SC");

        final MesorregiaoDTO mesorregiao = new MesorregiaoDTO();
        mesorregiao.setId(1L);
        mesorregiao.setNome("Vale do Itajaí");
        mesorregiao.setUf(uf);

        final MicrorregiaoDTO microrregiao = new MicrorregiaoDTO();
        microrregiao.setId(1L);
        microrregiao.setNome("Parque Nacional da Serra do Itajaí");
        microrregiao.setMesorregiao(mesorregiao);

        final MunicipioDTO municipio = new MunicipioDTO();
        municipio.setId(1L);
        municipio.setNome("Blumenau");
        municipio.setMicrorregiao(microrregiao);
        municipio.setRegiaoImediata(new RegiaoImediataDTO());

        final CityDTO result = new CityDTO(municipio);

        assertThat(result.getIdEstado()).isEqualTo(uf.getId());
        assertThat(result.getSiglaEstado()).isEqualTo(uf.getSigla());
        assertThat(result.getNomeMesorregiao()).isEqualTo(mesorregiao.getNome());
        assertThat(result.getRegiaoNome()).isEqualTo(regiao.getNome());
        assertThat(result.getNomeCidade()).isEqualTo(municipio.getNome());
        assertThat(result.getNomeFormatado()).isEqualTo(municipio.getNome() + "/" + uf.getSigla());
    }

}
