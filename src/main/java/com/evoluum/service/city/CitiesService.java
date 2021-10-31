package com.evoluum.service.city;

import com.evoluum.dto.fields.CityDTO;
import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.UfDTO;
import com.evoluum.exception.EvoluumException;
import com.evoluum.service.ibge.IbgeService;
import com.evoluum.utils.CSVHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitiesService {

    private static final Logger logger = LoggerFactory.getLogger(CitiesService.class);

    @Autowired
    private IbgeService ibgeService;

    /**
     * Retorna uma lista de bytes com os dados do JSON.
     *
     * @param uf Unidade da Federação
     * @return Lista de bytes
     */
    public byte[] generateJsonFile(final @Nullable String uf) {
        try {
            logger.info("[JSON] Gerando arquivo...");
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsBytes(getCities(uf));
        } catch (JsonProcessingException e) {
            logger.error("Erro ao gerar JSON", e);
            throw new EvoluumException("Ocorreu algum erro ao gerar o JSON!");
        }
    }

    /**
     * Retorna um OutputStream, os bytes serão utilizados para o arquivo CSV.
     *
     * @param uf Unidade da Federação
     * @return {@link ByteArrayOutputStream}
     */
    public ByteArrayOutputStream generateCsvFile(final @Nullable String uf) {
        try {
            logger.info("[CSV] Gerando arquivo...");
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final CSVFormat csvFormat = CSVHelper.getCsvFormat("idEstado", "siglaEstado", "regiaoNome", "nomeCidade", "nomeMesorregiao", "nomeFormatado");
            final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output, StandardCharsets.UTF_16LE), csvFormat);

            CSVHelper.printRecords(getCities(uf), printer);
            printer.flush();
            logger.info("[CSV] Arquivo gerado!");

            return output;
        } catch (IOException e) {
            logger.error("Erro ao gerar CSV", e);
            throw new EvoluumException("Ocorreu algum erro ao gerar o CSV!");
        }
    }

    /**
     * Retorna o ID da cidade a partir do nome.
     * Caso a cidade já tenha sido pesquisa nos ultimos 15 minutos, utilizaremos cache para retornar o ID.
     *
     * @param nomeCidade Nome da cidade
     * @return {@link Long} Id da cidade
     */
    @Cacheable(value = "cities")
    public Long getCityIdByName(final String nomeCidade) {
        final List<MunicipioDTO> municipioList = getAllMunicipios();
        final Optional<MunicipioDTO> municipio = municipioList.stream().filter(t -> t.getNome().equals(nomeCidade)).findFirst();

        if (municipio.isEmpty()) {
            throw new EvoluumException(String.format("Nenhuma cidade encontrada com o nome: %s", nomeCidade));
        }

        return municipio.get().getId();
    }

    /**
     * Realiza a validação da Unidade da Federação (UF) e busca as cidades.
     * <p>
     * Caso possua UF, buscamos apenas as cidades daquele estado.
     * Caso não possua UF ou seja inválida, buscamos as cidades de todos estados do Brasil.
     *
     * @param uf Unidade da Federação
     * @return {@link List<CityDTO>}
     */
    List<CityDTO> getCities(final @Nullable String uf) {
        if (StringUtils.isBlank(uf) || StringUtils.isNumeric(uf) || uf.length() != 2) {
            return getAllCities();
        }

        return getCitiesByUf(uf);
    }

    /**
     * Busca todos municípios do Brasil e depois converte para uma lista de cidades.
     *
     * @return {@link List<CityDTO>}
     */
    List<CityDTO> getAllCities() {
        final List<MunicipioDTO> municipioList = getAllMunicipios();
        return municipioList.stream().map(CityDTO::new).collect(Collectors.toList());
    }

    /**
     * Busca as cidades de uma determina Unidade da Federação.
     *
     * @param uf Unidade da Federação
     * @return {@link List<CityDTO>}
     */
    List<CityDTO> getCitiesByUf(final String uf) {
        final List<MunicipioDTO> municipioList = ibgeService.getMunicipiosByUf(uf);
        return municipioList.stream().map(CityDTO::new).collect(Collectors.toList());
    }

    /**
     * Busca todas as UF do Brasil, depois todos municípios e por último junta todos em uma lista.
     *
     * @return {@link List<MunicipioDTO>}
     */
    List<MunicipioDTO> getAllMunicipios() {
        final List<UfDTO> states = ibgeService.getStates();
        final List<MunicipioDTO> municipioList = new ArrayList<>();

        states.forEach(state -> municipioList.addAll(ibgeService.getMunicipiosByUf(state.getSigla())));

        return municipioList;
    }

}
