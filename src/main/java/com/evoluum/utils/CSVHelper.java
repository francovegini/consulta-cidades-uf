package com.evoluum.utils;

import com.evoluum.dto.fields.CityDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CSVHelper {

    private static final Logger logger = LoggerFactory.getLogger(CSVHelper.class);

    /**
     * Define o cabeçalho de acordo com os parâmetros e define o delimitador para ponto e vírgula (;).
     *
     * @param header Cabeçalho do CSV
     * @return {@link CSVFormat}
     */
    public static CSVFormat getCsvFormat(final String... header) {
        return CSVFormat.EXCEL.builder()
                .setHeader(header)
                .setDelimiter(";")
                .build();
    }

    /**
     * Insere os registros dos municípios no CSV.
     *
     * @param cityList Lista de municípios
     * @param printer  CSV
     */
    public static void printRecords(final List<CityDTO> cityList, final CSVPrinter printer) {
        cityList.forEach(city -> {
            try {
                printer.printRecord(
                        city.getIdEstado(),
                        city.getSiglaEstado(),
                        city.getRegiaoNome(),
                        city.getNomeCidade(),
                        city.getNomeMesorregiao(),
                        city.getNomeFormatado());
            } catch (IOException e) {
                logger.error("Erro ao inserir registro do município: {}", city.getNomeFormatado());
            }
        });
    }
}
