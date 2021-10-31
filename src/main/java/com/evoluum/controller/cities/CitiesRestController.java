package com.evoluum.controller.cities;

import com.evoluum.service.city.CitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Api
@RestController
@RequestMapping("/api/cities")
public class CitiesRestController {

    private static final String FILENAME = "cities";

    @Autowired
    private CitiesService service;

    /**
     * Retorna um arquivo JSON com as devidas cidades.
     * <p>
     * Caso possua UF, buscamos apenas as cidades daquele estado.
     * Caso não possua UF ou seja inválida, buscamos as cidades de todos estados do Brasil.
     *
     * @param uf Unidade da Federação
     * @return {@link InputStreamResource} Arquivo JSON para download
     */
    @ApiOperation(value = "Retorna um arquivo JSON com as devidas cidades.")
    @GetMapping("/json")
    public ResponseEntity<InputStreamResource> getJsonFile(@RequestParam(required = false) String uf) {
        final byte[] citiesBytes = service.generateJsonFile(uf);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME + ".json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(citiesBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(citiesBytes)));
    }

    /**
     * Retorna um arquivo CSV com as devidas cidades.
     * <p>
     * Caso possua UF, buscamos apenas as cidades daquele estado.
     * Caso não possua UF ou seja inválida, buscamos as cidades de todos estados do Brasil.
     *
     * @param uf       Unidade da Federação
     * @param response Response que será retornado para o client
     */
    @ApiOperation(value = "Retorna um arquivo CSV com as devidas cidades.")
    @GetMapping("/csv")
    public void getCsvFile(@RequestParam(required = false) String uf, HttpServletResponse response) {
        try {
            final ByteArrayOutputStream output = service.generateCsvFile(uf);

            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME + ".csv");
            response.setHeader("x-filename", FILENAME + ".csv");
            response.setContentLength(output.toByteArray().length);
            response.getOutputStream().write(output.toByteArray());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao realizar download do CSV", e);
        }
    }

    /**
     * Retorna o ID da cidade a partir do nome.
     * Caso a cidade já tenha sido pesquisa nos ultimos 15 minutos, utilizaremos cache para retornar o ID.
     *
     * @param nomeCidade Nome da cidade
     * @return {@link Long} Id da cidade
     */
    @ApiOperation(value = "Retorna o ID da cidade a partir do nome.")
    @GetMapping("/getCityId")
    public Long getCityId(@RequestParam String nomeCidade) {
        return service.getCityIdByName(nomeCidade);
    }

}
