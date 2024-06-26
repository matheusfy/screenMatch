package io.github.matheusfy.screanmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Conversor implements IConverteDados {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (IOException e) {
            // TODO: Trocar a exception por um InvalidFormatException
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

    public boolean isValidResponse(String  json){

        try {
            String response = mapper.readTree(json).get("Response").asText();
            return Boolean.parseBoolean(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDate omdbDateToLocalDate(String omdbDate){
        return LocalDate.parse(omdbDate, DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("pt-br", "BR", "dd MMM yyyy")));
    }

    public static String LocalDateToBrFormat(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
