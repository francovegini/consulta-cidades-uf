package com.evoluum.utils;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVHelperTest {

    @Test
    public void getCsvFormat() {
        final CSVFormat format = CSVHelper.getCsvFormat("idEstado", "siglaEstado", "regiaoNome", "nomeCidade", "nomeMesorregiao", "nomeFormatado");

        assertEquals(6, format.getHeader().length);
        assertEquals(";", format.getDelimiterString());
    }

}
