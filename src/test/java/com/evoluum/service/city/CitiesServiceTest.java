package com.evoluum.service.city;

import com.evoluum.dto.fields.CityDTO;
import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.UfDTO;
import com.evoluum.exception.EvoluumException;
import com.evoluum.service.ibge.IbgeService;
import com.evoluum.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CitiesServiceTest {

    @Spy
    @InjectMocks
    private CitiesService service;

    @Mock
    private IbgeService ibgeService;

    protected final TestUtils utils = new TestUtils();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void generateJsonFile() {
        final String uf = "SC";
        final CityDTO city = new CityDTO(utils.getMunicipio(1L, "Itapema"));

        Mockito.doReturn(List.of(city)).when(service).getCities(uf);

        final byte[] result = service.generateJsonFile(uf);
        assertEquals(150, result.length);
    }

    @Test
    public void generateCsvFile_withCitiesListShouldReturnRecords() {
        final String uf = "SC";
        final CityDTO city = new CityDTO(utils.getMunicipio(1L, "Itapema"));

        Mockito.doReturn(List.of(city)).when(service).getCities(uf);

        final ByteArrayOutputStream output = service.generateCsvFile(uf);
        assertEquals(250, output.toByteArray().length);
    }

    @Test
    public void generateCsvFile_emptyCitiesListShouldReturnOnlyHeader() {
        final String uf = "SP";

        Mockito.doReturn(Collections.emptyList()).when(service).getCities(uf);

        final ByteArrayOutputStream output = service.generateCsvFile(uf);
        assertEquals(148, output.toByteArray().length);
    }

    @Test(expected = EvoluumException.class)
    public void getCityIdByName_shouldReturnEvoluumException() {
        final MunicipioDTO municipio = utils.getMunicipio(1L, "Jaragua do Sul");
        final MunicipioDTO municipio2 = utils.getMunicipio(2L, "Luis Alves");

        Mockito.doReturn(List.of(municipio, municipio2)).when(service).getAllMunicipios();

        service.getCityIdByName("Gaspar");
    }

    @Test
    public void getCityIdByName_shouldReturnCityId() {
        final MunicipioDTO municipio = utils.getMunicipio(1L, "Timbó");
        final MunicipioDTO municipio2 = utils.getMunicipio(2L, "Gaspar");

        Mockito.doReturn(List.of(municipio, municipio2)).when(service).getAllMunicipios();

        final Long result = service.getCityIdByName("Gaspar");
        assertEquals(2L, result);
    }

    @Test
    public void getCities_invalidUfShouldCallAll() {
        final String uf = "XXX";

        Mockito.doReturn(Collections.emptyList()).when(service).getAllCities();

        service.getCities(uf);
        verify(service).getAllCities();
    }

    @Test
    public void getCities_emptyStringShouldCallAll() {
        final String uf = "";

        Mockito.doReturn(Collections.emptyList()).when(service).getAllCities();

        service.getCities(uf);
        verify(service).getAllCities();
    }

    @Test
    public void getCities_shouldCallByUf() {
        final String uf = "SC";

        Mockito.doReturn(Collections.emptyList()).when(service).getCitiesByUf(uf);

        service.getCities(uf);
        verify(service).getCitiesByUf(uf);
    }

    @Test
    public void getAllCities_shouldReturnEmptyList() {
        Mockito.doReturn(Collections.emptyList()).when(service).getAllMunicipios();

        final List<CityDTO> result = service.getAllCities();
        assertEquals(0, result.size());
    }

    @Test
    public void getAllCities_shouldReturnThreeCities() {
        final MunicipioDTO municipio = utils.getMunicipio(1L, "Blumenau");
        final MunicipioDTO municipio2 = utils.getMunicipio(2L, "Gaspar");
        final MunicipioDTO municipio3 = utils.getMunicipio(2L, "Indaial");

        Mockito.doReturn(List.of(municipio, municipio2, municipio3)).when(service).getAllMunicipios();

        final List<CityDTO> result = service.getAllCities();
        assertEquals(3, result.size());
    }

    @Test
    public void getCitiesByUf_shouldReturnTwoCities() {
        final String uf = "SC";
        final MunicipioDTO municipio = utils.getMunicipio(1L, "Blumenau");
        final MunicipioDTO municipio2 = utils.getMunicipio(2L, "Gaspar");

        when(ibgeService.getMunicipiosByUf(uf)).thenReturn(List.of(municipio, municipio2));

        final List<CityDTO> result = service.getCitiesByUf(uf);
        assertEquals(2, result.size());
        assertEquals("Blumenau/SC", result.get(0).getNomeFormatado());
        assertEquals("Gaspar/SC", result.get(1).getNomeFormatado());
    }

    @Test
    public void getCitiesByUf_invalidUfShouldReturnEmptyList() {
        final String uf = "XX";

        when(ibgeService.getMunicipiosByUf(uf)).thenReturn(Collections.emptyList());

        final List<CityDTO> result = service.getCitiesByUf(uf);
        assertEquals(0, result.size());
    }

    @Test
    public void getAllMunicipios_success() {
        final UfDTO uf = utils.getUf(1L, "Paraná", "PR");
        final UfDTO uf2 = utils.getUf(2L, "Santa Catarina", "SC");
        final UfDTO uf3 = utils.getUf(3L, "Rio Grande do Sul", "RS");

        when(ibgeService.getStates()).thenReturn(List.of(uf, uf2, uf3));

        service.getAllMunicipios();

        verify(ibgeService, times(3)).getMunicipiosByUf(anyString());
    }

    @Test(expected = EvoluumException.class)
    public void getAllMunicipios_shouldReturnException() {
        when(ibgeService.getStates()).thenThrow(new EvoluumException("PI do IBGE está indisponível!"));

        service.getAllMunicipios();
    }
}
