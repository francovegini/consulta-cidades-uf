package com.evoluum.service.ibge;

import com.evoluum.dto.ibge.MunicipioDTO;
import com.evoluum.dto.ibge.UfDTO;
import com.evoluum.exception.EvoluumException;
import com.evoluum.service.city.CitiesService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Service responsável por chamar API's externas do IBGE.
 */
@Service
public class IbgeService {

    private static final Logger logger = LoggerFactory.getLogger(CitiesService.class);
    private static final String API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";
    private static final String EVOLUUM = "evoluum";

    /**
     * Busca todos os estados do Brasil.
     *
     * @return {@link List<UfDTO>} Lista de estados do Brasil
     */
    @CircuitBreaker(name = EVOLUUM, fallbackMethod = "fallback")
    public List<UfDTO> getStates() {
        try {
            final URL url = new URL(API_URL);
            final RestTemplate restTemplate = new RestTemplate();

            return parseStatesToList(restTemplate.getForEntity(url.toURI(), UfDTO[].class).getBody());
        } catch (Exception ex) {
            logger.error("[REQUEST] Erro ao buscar estados - Erro: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Busca os municípios de uma determinada Unidade da Federação (UF).
     *
     * @param uf Unidade da Federação (UF)
     * @return {@link List<MunicipioDTO>} Lista de municípios
     */
    @CircuitBreaker(name = EVOLUUM, fallbackMethod = "fallback")
    public List<MunicipioDTO> getMunicipiosByUf(final String uf) {
        try {
            final URL url = new URL(API_URL + uf + "/municipios");
            final RestTemplate restTemplate = new RestTemplate();

            return parseCitiesToList(restTemplate.getForEntity(url.toURI(), MunicipioDTO[].class).getBody());
        } catch (Exception ex) {
            logger.error("[REQUEST] Erro ao buscar as cidades do estado {} - Erro: {}", uf, ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    List<UfDTO> parseStatesToList(UfDTO[] states) {
        if (states == null || states.length == 0) {
            return Collections.emptyList();
        }

        return List.of(states);
    }

    List<MunicipioDTO> parseCitiesToList(MunicipioDTO[] cities) {
        if (cities == null || cities.length == 0) {
            return Collections.emptyList();
        }

        return List.of(cities);
    }

    public List<MunicipioDTO> fallback(Exception ex) {
        // Retornando uma exceção tratada para o client.
        // Uma ideia seria salvar os retornos no cache, de forma que, quando o sistema ficasse indisponível, pudessemos retornar estas informações.
        throw new EvoluumException(String.format("API do IBGE está indisponível!\nErro: %s", ex.getMessage()));
    }

}
