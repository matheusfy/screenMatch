package io.github.matheusfy.screanmatch.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonParser;
import io.github.matheusfy.screanmatch.model.DadosSerieDTO;
import org.springframework.boot.json.JacksonJsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ConsumoApi {

    public String obterDado(String endereco){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).GET().build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = response.body();
        ConverterDados conversorDados = new ConverterDados();
        DadosSerieDTO dadosSerie = conversorDados.obterDados(json, DadosSerieDTO.class);


        return dadosSerie.toString();

    }
}
