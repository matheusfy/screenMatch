package io.github.matheusfy.screanmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ConverterDados implements IConverteDados {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getType(String json){

        String  tipo;

        try {
            tipo = mapper.readTree(json).get("Type").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return tipo;
    }

    public Integer strToInt(String string){
        try{
            return Integer.valueOf(string);
        } catch (NumberFormatException error){
            System.out.println("Não é possivel realizar a conversão para numero: " + error.getMessage());
        }
        return null;
    }
}
