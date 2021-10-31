package com.evoluum.utils;

import com.evoluum.dto.ibge.MesorregiaoDTO;
import com.evoluum.dto.ibge.MicrorregiaoDTO;
import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.RegiaoDTO;
import com.evoluum.dto.ibge.RegiaoImediataDTO;
import com.evoluum.dto.ibge.UfDTO;

public class TestUtils {

    public UfDTO getUf(Long id, String nome, String sigla) {
        final RegiaoDTO regiao = new RegiaoDTO();
        regiao.setId(1L);
        regiao.setNome("Região Sul");
        regiao.setSigla("Sul");

        final UfDTO uf = new UfDTO();
        uf.setId(id);
        uf.setNome(nome);
        uf.setSigla(sigla);
        uf.setRegiao(regiao);

        return uf;
    }

    public MunicipioDTO getMunicipio(Long idMunicipio, String nomeMunicipio) {
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
        municipio.setId(idMunicipio);
        municipio.setNome(nomeMunicipio);
        municipio.setMicrorregiao(microrregiao);
        municipio.setRegiaoImediata(new RegiaoImediataDTO());

        return municipio;
    }

}
