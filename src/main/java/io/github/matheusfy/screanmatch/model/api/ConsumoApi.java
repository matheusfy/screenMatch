package io.github.matheusfy.screanmatch.model.api;

import io.github.matheusfy.screanmatch.http.HttpHandler;
import io.github.matheusfy.screanmatch.model.entity.Episodio;
import io.github.matheusfy.screanmatch.model.dtos.EpisodioDTO;
import io.github.matheusfy.screanmatch.model.dtos.DadosFilmeDTO;
import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.dtos.TemporadaDTO;
import io.github.matheusfy.screanmatch.service.Conversor;

import java.util.*;
import java.util.stream.Collectors;

public class ConsumoApi {

    private final Conversor conversor;
    HttpHandler httpHandler = new HttpHandler();
    public ConsumoApi(){
        this.conversor  = new Conversor();
    }

    public String  obterDado(String apiUri){
        return httpHandler.buildAndSendRequest(apiUri);
    }

    public void obterDadosFilme(String apiUri){

        String json = obterDado(apiUri);

        if(conversor.isValidResponse(json)){
            DadosFilmeDTO filme = buscaDados(json, DadosFilmeDTO.class);
            System.out.println(filme.toString());
        } else {
            // TODO: Utilizar logger e exception para informar usuario que não foi encontrado a serie buscada
            System.out.println("Filme não encontrado");
        }

    }


    public Optional<SerieDTO> obterDadosSerie(String apiUri){
        String  json = "";

        json = obterDado(apiUri);

        if ((!json.equals("")) && (conversor.isValidResponse(json))){
            if(conversor.getType(json).equals("series")){
                return Optional.ofNullable(buscaDados(json, SerieDTO.class));
            } else {
                //TODO: Lança exception avisando usuario que é um filme
                System.out.println("A série buscada é um filme.");
                return Optional.empty();
            }
        } else {
            //TODO: Mudar para warning e adicionar uma exception
            System.out.println("Serie nao encontrada. Verifique o nome digitado.");
            return Optional.empty();
        }
    }

    public List<Episodio> obterEpisodiosSerie(String apiUri){
        Optional<SerieDTO> serie = obterDadosSerie(apiUri);

        if(serie.isPresent()){
            List<TemporadaDTO> lstTemporadas = getTemporadas(apiUri, serie.get().totalTemporadas());
            List<Episodio> episodios = getEpisodios(lstTemporadas);
//            episodios.forEach(System.out::println);
            return episodios;
        }

        return null;


//            String texto = """
//                            Digite 1 para buscar por um episódio.
//                            Digite 2 para calcular a média de avaliação das temporadas.
//                            Digite 3 para mostrar os 5 melhores episódios da série.
//                            """;
//
//            System.out.println(texto);

//            int opcao = scanner.nextInt();

//            switch (opcao){
//                case 1 ->{
//                    scanner.nextLine(); // flush do buffer
//                    System.out.println("Digite o nome do episódio: ");
//                    String  episodioName = scanner.nextLine();
//
//                    Optional<Episodio> episodio = buscaEpisodio(episodioName, episodios);
//
//                    if (episodio.isPresent()){
//                        System.out.println(episodio.get().toString());
//                    } else {
//                        System.out.println("Episódio não encontrado");
//                    }
//                }
//                case 2 -> mostrarMediaTemporadas(episodios);
//                case 3 -> getMelhores5Episodios(lstTemporadas).forEach(System.out::println);
//            }

    }

    public List<TemporadaDTO> getTemporadas(String uri, Integer temporadas){

        List<TemporadaDTO> lstTemporadas = new ArrayList<>();

        for(int temporada = 1; temporada <= temporadas; temporada++){
            String linkTemporada = uri + "&Season=%d".formatted(temporada);

            TemporadaDTO temporadaDTO = buscaDados(getTemporadaBody(linkTemporada), TemporadaDTO.class);
            lstTemporadas.add(temporadaDTO);
        }
        return lstTemporadas;
    }


    private <T> T buscaDados(String json, Class<T> classe){
        return conversor.obterDados(json, classe);
    }

    private String getTemporadaBody(String uriTemporada){
        return httpHandler.buildAndSendRequest(uriTemporada);
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
