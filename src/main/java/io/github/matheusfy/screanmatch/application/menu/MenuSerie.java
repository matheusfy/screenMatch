package io.github.matheusfy.screanmatch.application.menu;

import io.github.matheusfy.screanmatch.model.api.ConsumoApi;
import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.entity.Episodio;
import io.github.matheusfy.screanmatch.model.entity.Serie;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import io.github.matheusfy.screanmatch.model.repository.EpisodioRepository;
import io.github.matheusfy.screanmatch.model.repository.SerieRepository;

import java.lang.invoke.StringConcatException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static io.github.matheusfy.screanmatch.application.Principal.buildUri;
import static io.github.matheusfy.screanmatch.model.enums.Categoria.listarCategoria;

public class MenuSerie {

    private final SerieRepository serieRepository;
    private final EpisodioRepository episodioRepository;
    private final Scanner cmd;
    private final ConsumoApi api;

    public MenuSerie(SerieRepository serieRepository, EpisodioRepository episodioRepository, Scanner cmd, ConsumoApi api){
        this.api = api;
        this.cmd = cmd;
        this.serieRepository = serieRepository;
        this.episodioRepository = episodioRepository;
    }

    public void exibeMenuSerie(){

        String opcao = "-1";
        String menu = """
            1 - Buscar série na web\s
            2 - Buscar episódio\s
            3 - Lista séries buscadas\s
            4 - Buscar série por título\s
            5 - Buscar série por atores\s
            6 - Buscar série por categoria\s
            7 - Buscar as melhor 5 séries avaliadas\s
            8 - Busque uma série pela temporada e pela avaliação\s
            9 - Buscar episódio por trecho \s
            10 - Buscar top 5 episodios \s
            0 - Sair
            """;
        while(!opcao.equals("0")){
            System.out.println(menu);
            opcao = cmd.nextLine();

            switch (opcao){
                case "1" ->{
                    buscarSerieNaWeb(getUriSerie(getNomeSerie()));
                }
                case "2" ->{
                    String nomeSerie = getNomeSerie();
                    buscaEpisodioPorSerie(nomeSerie);
                }
                case "3" -> listarSeriesBuscadas();
                case "4" -> buscarSeriePorTitulo();
                case "5" -> buscarSeriePorAtor();
                case "6" -> buscarSeriesPorCategoria();
                case "7" -> buscarTop5Series();
                case "8" -> buscarSeriePorTemporadaEAvaliacao();
                case "9" -> buscarEpisodioPorTrecho();
                case "10" -> buscarTop5Episodios();
                case "0" -> System.out.println("Saindo do menu de série");
            }
        }

    }

    private void buscarTop5Series() {

        List<Serie> series = serieRepository.findTop5ByOrderByAvaliacaoDesc();
        if(!series.isEmpty()){
            series.forEach(System.out::println);
        } else {
            System.out.println("Não temos nenhuma série no nosso banco de dados");
        }
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Categorias possíveis: " );
        listarCategoria();

        System.out.println("Digite uma categoria: ");
        String categoria = cmd.nextLine();

        try {
            List<Serie> seriesCategoria = serieRepository.findByCategoria(Categoria.fromPortugues(categoria));
            if(!seriesCategoria.isEmpty()){
                seriesCategoria.forEach(System.out::println);
            } else {
                System.out.printf("Série não encontrada para categoria %s%n", categoria);
            }
        } catch (Exception error){
            System.out.println("Não foi possivel obter a lista. Erro: " + error.getMessage());
        }
    }

    private Optional<Serie> seriePresentOnList(String titulo){
        Optional<Serie> serie = serieRepository.findByTituloIgnoreCase(titulo.trim());
        serie.ifPresent(System.out::println);
        return serie;
    }

    private String getNomeSerie(){
        System.out.println("Digite o nome da série: ");
        return cmd.nextLine();
    }

    private String getUriSerie(String nome){
        return buildUri(nome);
    }

    private Serie buscarSerieNaWeb(String uri){

        Optional<SerieDTO> serieDTO = Optional.empty();
        Serie serieBuscada = null;
        try{
            serieDTO = api.obterDadosSerie(uri);
        } catch (RuntimeException error){
            System.out.println("Erro na conversão dos dados." + error.getMessage());
        }

        if(serieDTO.isPresent()){
            System.out.println("Informação serie: " + serieDTO.get());
            // Verificar se a série retornada não existe realmente no banco
            Optional<Serie> serie =  seriePresentOnList(serieDTO.get().titulo());
            if(serie.isEmpty()){
                serieBuscada = saveSerieToDB(new Serie(serieDTO.get()));
            } else {
                System.out.println("API nos retornou uma série que já existe no nosso banco");
                serieBuscada = serie.get();
            }
        }
        return serieBuscada;
    }

    private Serie saveSerieToDB(Serie serie){
        return serieRepository.save(serie);
    }

    private void buscaEpisodioPorSerie(String nomeSerie){


        Optional<Serie> serie = seriePresentOnList(nomeSerie);

        if(serie.isPresent()){
            Serie serieEncontrada = serie.get();
            System.out.println("Buscando episodios no banco. Serie id: " + serieEncontrada.getId());
            TryGetAndSaveEpisodes(serieEncontrada, nomeSerie);
        } else {
            System.out.println("Ainda nao temos informações sobre esta série. Obtendo mais informações...");
            serie = Optional.ofNullable(buscarSerieNaWeb(getUriSerie(nomeSerie)));

            if(serie.isPresent()){
                Serie serieBuscada = serie.get();
                TryGetAndSaveEpisodes(serieBuscada,nomeSerie);
            }
        }
    }

    private void TryGetAndSaveEpisodes(Serie serie, String nomeSerie){
        List<Episodio> episodios;
        episodios = episodioRepository.findBySerieId(serie.getId());
        if(episodios.isEmpty()){
            // Não encontramos episodios da série no banco. Buscamos na web
            getEpisodesAndSave(serie, nomeSerie);
        } else {
            System.out.println("Encontramos episodios disponíveis para esta série no banco.");
            episodios.forEach(System.out::println);
        }
    }

    private void getEpisodesAndSave(Serie serie, String nomeSerie){
        List<Episodio> episodios;
        episodios = api.obterEpisodiosSerie(getUriSerie(nomeSerie));
        saveEpisodesToDB(episodios, serie);
        episodios.forEach(System.out::println);
    }

    private void saveEpisodesToDB(List<Episodio> episodios, Serie serie){
        serie.setEpisodios(episodios);
        episodioRepository.saveAllAndFlush(episodios);
    }

    private void listarSeriesBuscadas(){
        serieRepository.findAll().stream()
            .sorted(Comparator.comparing(Serie::getCategoria))
            .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.println("Digite o algo contido no nome da serie para buscar: ");
        String palavra = cmd.nextLine();

        List<Serie> series = serieRepository.findByTituloContainingIgnoreCase(palavra);
        showListSeries(series, "Serie não encontrada com a palavra: ".formatted(palavra));
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do autor para buscar séries: ");
        String nomeAutor = cmd.nextLine();

        List<Serie> series = serieRepository.findByAtoresContainingIgnoreCase(nomeAutor);
        showListSeries(series, "Nenhuma série encontrada com o ator: ".formatted(nomeAutor));
    }

    private void showListSeries(List<Serie> series, String notFindMsg){
        if(series.isEmpty()){
            System.out.println(notFindMsg);
        } else {
            System.out.println("Series encontradas: ");
            series.forEach(System.out::println);
        }
    }

    private void buscarSeriePorTemporadaEAvaliacao() {
        System.out.println("Digite uma avaliação mínima: ");
        Double avaliacao = cmd.nextDouble();
        cmd.nextLine();

        System.out.println("Digite uma temporada máxima: ");
        Integer temporada = cmd.nextInt();
        cmd.nextLine();

        List<Serie> series = serieRepository.findByTemporadaAndAvaliacao(temporada, avaliacao);
        if(!series.isEmpty()){
            System.out.println("Séries encontradas: ");
            series.forEach(serie -> System.out.printf("Serie: %s - Avaliação: %.2f - Temporadas: %d%n", serie.getTitulo(), serie.getAvaliacao(), serie.getTotalTemporadas()));
        } else {
            System.out.println("Nenhuma série encontradas");
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite um trecho do episódio a ser buscado");
        String trecho = cmd.nextLine();

        try{
            List<Episodio> episodios = episodioRepository.findEpisodioPorTrecho(trecho);

            if(!episodios.isEmpty()){
                episodios.forEach(e ->
                    System.out.println("Serie: %s - Temporada: %d - Episodio: %d - %s"
                        .formatted(e.getSerie().getTitulo(), e.getTemporada(), e.getId(), e.getTitulo())));
            } else {
                System.out.println("Não encontramos nenhum episódio com o trecho: " + trecho);
            }
        } catch (Exception error){
            System.out.println("Erro na tentativa de obter episodios: " + error.getMessage());
        }

    }

    private void buscarTop5Episodios() {
        System.out.println("Informe uma série pra buscar os melhores episódios: ");
        String nomeSerie = cmd.nextLine();

        List<Episodio> episodios = episodioRepository.findTop5BySerieOrderByAvaliacaoDesc(nomeSerie);
        if(!episodios.isEmpty()){
            episodios.forEach(e -> {
                System.out.println("Avaliacao: %.2f - Temporada: %d - Episodio: %d - Titulo: %s".formatted(
                    e.getAvaliacao() , e.getTemporada(), e.getId(), e.getTitulo()));
            });
        } else {
            System.out.println("Nenhuma série encontrada. Série buscada: " + nomeSerie);
        }

    }



}
