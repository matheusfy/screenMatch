package io.github.matheusfy.screanmatch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matheusfy.screanmatch.model.DadosSerieDTO;

import java.io.IOException;

public class ConverterDados implements IConverteDados {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            JsonNode node = mapper.readTree(json);
            System.out.println("Pegando o título do retorno: " + node.get("Title").asText());

            return mapper.readValue(json, classe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
