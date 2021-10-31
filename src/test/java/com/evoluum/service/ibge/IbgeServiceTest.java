package com.evoluum.service.ibge;

import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.UfDTO;
import com.evoluum.exception.EvoluumException;
import com.evoluum.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class IbgeServiceTest {

    @InjectMocks
    private IbgeService service;

    protected final TestUtils utils = new TestUtils();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getStates() {
        final List<UfDTO> states = service.getStates();

        Assertions.assertEquals(27, states.size());
    }

    @Test
    public void getMunicipiosByUf() {
        final String uf = "SC";
        final List<MunicipioDTO> municipios = service.getMunicipiosByUf(uf);

        Assertions.assertEquals(295, municipios.size());
    }

    @Test
    public void parseStatesToList_withoutStateShouldReturnEmptyList() {
        final List<UfDTO> result = service.parseStatesToList(new UfDTO[0]);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void parseStatesToList_withStateShouldReturnAElement() {
        UfDTO[] states = new UfDTO[1];
        states[0] = utils.getUf(1L, "Santa Catarina", "SC");

        final List<UfDTO> result = service.parseStatesToList(states);

        Assert.assertEquals(1, result.size());
    }

    @Test
    public void parseCitiesToList_withoutMunicipioShouldReturnEmptyList() {
        final List<MunicipioDTO> result = service.parseCitiesToList(new MunicipioDTO[0]);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void parseCitiesToList_withMunicipioShouldReturnAElement() {
        MunicipioDTO[] municipios = new MunicipioDTO[1];
        municipios[0] = utils.getMunicipio(1L, "Blumenau");

        final List<MunicipioDTO> result = service.parseCitiesToList(municipios);

        Assert.assertEquals(1, result.size());
    }

    @Test(expected = EvoluumException.class)
    public void fallback() {
        service.fallback(new RuntimeException("Erro"));
    }
}
