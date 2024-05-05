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
                    Scanner scanner = new Scanner(System.in);

                    List<TemporadaDTO> lstTemporadas = getTemporadas(apiUri, conversor.strToInt(serie.totalTemporadas()));
                    List<Episodio> episodios = getEpisodios(lstTemporadas);
                    String texto = """
                            Digite 1 para buscar por um episódio.
                            Digite 2 para calcular a média de avaliação das temporadas.
                            Digite 3 para mostrar os 5 melhores episódios da série.
                            """;

                    System.out.println(texto);

                    int opcao = scanner.nextInt();

                    switch (opcao){
                        case 1 ->{
                                scanner.nextLine(); // flush do buffer
                                System.out.println("Digite o nome do episódio: ");
                                String  episodioName = scanner.nextLine();

                                Optional<Episodio> episodio = buscaEpisodio(episodioName, episodios);

                                if (episodio.isPresent()){
                                    System.out.println(episodio.get().toString());
                                } else {
                                    System.out.println("Episódio não encontrado");
                                }
                        }
                        case 2 -> mostrarMediaTemporadas(episodios);
                        case 3 -> getMelhores5Episodios(lstTemporadas).forEach(System.out::println);
                    }

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

    public void mostrarMediaTemporadas(List<Episodio> episodios){

        Map<Integer, Double> mediaTemp = episodios.stream()
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));

        mediaTemp.forEach((key, value) -> System.out.println("Temporada: " + key + " Avaliação: " + value));
    }

}
