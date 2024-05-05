package io.github.matheusfy.screanmatch.service;

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
import java.util.*;
import java.util.stream.Collectors;

public class ConsumoApi {
    private final HttpClient client;
    private final ConverterDados conversor;

    public ConsumoApi(){
        this.client  = HttpClient.newHttpClient();
        this.conversor  = new ConverterDados();
    }

    private HttpRequest buildRequest(String uri){
        return HttpRequest.newBuilder().uri(URI.create(uri)).GET().build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request){
        HttpResponse<String> response;
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
            return response.body();
        }
        return "";
    }

    public void  obterDado(String apiUri){

        String json = buildAndSendRequest(apiUri);
        String tipo = conversor.getType(json);

        switch (tipo) {
            case "series" -> {
                SerieDTO serie = conversor.obterDados(json, SerieDTO.class);
                System.out.println("Informação serie: " + serie.toString());
                if (!serie.totalTemporadas().equals("N/A")) {
                    List<TemporadaDTO> lstTemporadas = getTemporadas(apiUri, conversor.strToInt(serie.totalTemporadas()));
                    List<Episodio> episodios = getEpisodios(lstTemporadas);
                    System.out.println(episodios);
                }
            }
            case "movie" -> {
                DadosFilmeDTO filme = conversor.obterDados(json, DadosFilmeDTO.class);
                System.out.println(filme.toString());
            }
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
                .toList();
    }

    public List<EpisodioDTO> getMelhores5Episodios(List<TemporadaDTO> lstTemporadas) {

        List<EpisodioDTO> allEpisodes = lstTemporadas.stream()
                .flatMap(temp -> temp.lstEpisodios().stream())
                .toList();

        return allEpisodes.stream()
                .filter(e->!e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodioDTO::avaliacao).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public Optional<Episodio> buscaEpisodio(String nomeEpisodio, List<Episodio> episodios){

        return episodios.stream()
                .filter(e -> e.getTitulo().contains(nomeEpisodio))
                .findFirst();
    }

}
