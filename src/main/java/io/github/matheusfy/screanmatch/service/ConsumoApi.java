package io.github.matheusfy.screanmatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matheusfy.screanmatch.model.Episodio;
import io.github.matheusfy.screanmatch.model.dtos.EpisodioDTO;
import io.github.matheusfy.screanmatch.model.dtos.DadosFilmeDTO;
import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.dtos.TemporadaDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private HttpResponse<String> sendRequest(HttpRequest request){
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
    }
        return response;
    }

    private String buildAndSendRequest(String uri){
        HttpResponse<String> response = sendRequest(buildRequest(uri));
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
                SerieDTO serie = conversor.obterDados(json, SerieDTO.class);
                System.out.println("Informação serie: " + serie.toString());
                if (serie.totalTemporadas() != "N/A"){
                    List<TemporadaDTO> lstTemporadas= getTemporadas(apiUri, conversor.strToInt(serie.totalTemporadas()));
//                    getMelhoresEpisodios(lstTemporadas);
                    getEpisodios(lstTemporadas);
                }

                break;
            case "movie":
                DadosFilmeDTO filme = conversor.obterDados(json, DadosFilmeDTO.class);
                System.out.println(filme.toString());
                break;
        }

    }

    public List<TemporadaDTO> getTemporadas(String uri, Integer temporadas){

        List<TemporadaDTO> lstTemporadas = new ArrayList<>();

        for(int temporada = 1; temporada <= temporadas; temporada++){
            String linkTemporada = uri + "&Season=%d";
            TemporadaDTO temporadaDTO = conversor.obterDados(buildAndSendRequest(linkTemporada.formatted(temporada)), TemporadaDTO.class);
            lstTemporadas.add(temporadaDTO);
        }

        return lstTemporadas;
    }

    public List<Episodio> getEpisodios(List<TemporadaDTO> lstTemporadas){

        return lstTemporadas.stream()
                .flatMap( t -> t.lstEpisodios().stream()
                        .map(e -> new Episodio(t.numero(), e)))
                .collect(Collectors.toList());
    }

    public List<EpisodioDTO> getMelhoresEpisodios(List<TemporadaDTO> lstTemporadas) {

        List<EpisodioDTO> allEpisodes = lstTemporadas.stream()
                .flatMap(temp -> temp.lstEpisodios().stream())
                .collect(Collectors.toList());

        List<EpisodioDTO> betEpisodes = allEpisodes.stream()
                .filter(e->!e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodioDTO::avaliacao).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return betEpisodes;
    }

}
