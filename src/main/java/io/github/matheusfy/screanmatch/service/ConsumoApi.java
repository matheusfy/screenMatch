package io.github.matheusfy.screanmatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matheusfy.screanmatch.model.DadosFilmeDTO;
import io.github.matheusfy.screanmatch.model.DadosSerieDTO;
import io.github.matheusfy.screanmatch.model.DadosTemporadaDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ConsumoApi {
    private HttpClient client;
    private ObjectMapper mapper;
    private ConverterDados conversor;

    public ConsumoApi(){
        this.client  = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.conversor  = new ConverterDados();
    }

    private HttpRequest buildRequest(String uri){
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).GET().build();
        return request;
    }

    private HttpResponse sendRequest(HttpRequest request){
        HttpResponse response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
    }
        return response;
    }

    private String buildAndSendRequest(String uri){
        HttpResponse response = sendRequest(buildRequest(uri));
        if (response!=null) {
            return response.body().toString();
        }
        return "";
    }

    public void obterDado(String apiUri){

        String json = buildAndSendRequest(apiUri);
        String tipo = conversor.getType(json);

        switch (tipo){
            case "series":
                DadosSerieDTO serie = conversor.obterDados(json, DadosSerieDTO.class);
                System.out.println("Informação serie: " + serie.toString());
//                List<DadosTemporadaDTO> lstTemporadas= getTemporadas(apiUri, serie.totalTemporadas());
                break;
            case "movie":
                DadosFilmeDTO filme = conversor.obterDados(json, DadosFilmeDTO.class);
                System.out.println(filme.toString());
                break;
        }

    }

    public List<DadosTemporadaDTO> getTemporadas(String uri, Integer temporadas){

        List<DadosTemporadaDTO> lstDadosTemporadas = new ArrayList<>();

        for(int temporada = 1; temporada <= temporadas; temporada++){
            String linkTemporada = uri + "&Season=%d";
            DadosTemporadaDTO temporadaDTO = conversor.obterDados(buildAndSendRequest(linkTemporada.formatted(temporada)), DadosTemporadaDTO.class);
            lstDadosTemporadas.add(temporadaDTO);
        }

        return lstDadosTemporadas;
    }

}
