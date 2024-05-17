package io.github.matheusfy.screanmatch.application;

import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.entity.Episodio;
import io.github.matheusfy.screanmatch.model.entity.Serie;
import io.github.matheusfy.screanmatch.model.api.ConsumoApi;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import io.github.matheusfy.screanmatch.model.repository.EpisodioRepository;
import io.github.matheusfy.screanmatch.model.repository.SerieRepository;

import java.util.*;

import static io.github.matheusfy.screanmatch.model.enums.Categoria.listarCategoria;

public class Principal {

    private final Scanner leitor;
    private final ConsumoApi api = new ConsumoApi();
    private final SerieRepository serieRepository;
    private final EpisodioRepository episodioRepository;

    private String API_KEY = "&apikey=%s";

    public Principal(SerieRepository serieRepository, EpisodioRepository episodioRepository){
        this.serieRepository = serieRepository;
        this.episodioRepository = episodioRepository;
        this.leitor = new Scanner(System.in);
        this.API_KEY = API_KEY.formatted(System.getenv("OMDB_APIKEY"));
    }

    public void exibeMenu(){

        String opcao = "-1";
        while(!opcao.equals("0")){

            System.out.println("1 - série \n2 - filme \n0 - Sair");
            opcao = leitor.nextLine();

            switch (opcao){
                case "1" -> exibeMenuSerie();
                case "2" ->{
                    System.out.println("Digite o nome do filme: ");
                    buscarFilme(buildUri(leitor.nextLine()));
                }

                case "0" -> System.out.println("Saindo do menu principal");
            }
        }
    }

    private void exibeMenuSerie(){

        String opcao = "-1";

        while(!opcao.equals("0")){
            System.out.println("1 - Buscar série \n2 - Buscar episódio \n3 - Lista séries buscadas \n4 - Buscar série por categoria \n99 - Limpar console\n0 - Sair");
            opcao = leitor.nextLine();
            
            switch (opcao){
                case "1" ->{

                    String nomeSerie = getNomeSerie();
                    if (seriePresentOnList(nomeSerie).isEmpty()){
                        buscarSerie(getUriSerie(nomeSerie));
                    }
                }
                case "2" ->{
                    String nomeSerie = getNomeSerie();
                    buscaEpisodioSerie(nomeSerie);
                }
                case "3" -> listarSeriesBuscadas();

                case "4" -> buscarSeriesCategoria();
                case "99" -> limpaConsole();
                case "0" -> System.out.println("Saindo do menu de série");
            }
        }

    }

    // FUNÇÕES SÉRIES
    private void listarSeriesBuscadas(){

        // TODO: criar outro tipo de listagem. Por exemplo: ordem alfabética, mais bem avaliados, com menor temporada etc...
        System.out.println("Listando por categoria: ");

        serieRepository.findAll().stream()
            .sorted(Comparator.comparing(Serie::getCategoria))
            .forEach(System.out::println);
    }

    private void buscarSeriesCategoria(){
        System.out.println("Categorias possíveis: " );
        listarCategoria();

        System.out.println("Digite uma categoria: ");
        String categoria = leitor.nextLine();

        try {
            List<Serie> seriesCategoria = serieRepository.findByCategoria(Categoria.fromString(categoria));
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
        return leitor.nextLine();
    }

    private String getUriSerie(String nome){
        return buildUri(nome);
    }

    private Serie buscarSerie(String uri){

        Optional<SerieDTO> serieDTO = Optional.empty();
        try{
            serieDTO = api.obterDadosSerie(uri);
        } catch (RuntimeException error){
            System.out.println("Erro na conversão dos dados." + error.getMessage());
        }

        if(serieDTO.isPresent()){
            System.out.println("Informação serie: " + serieDTO.get());
            Serie serie = new Serie(serieDTO.get());
             saveSerieToDB(serie);

             return serie;
        }
        return null;
    }

    private void saveSerieToDB(Serie serie){
        serieRepository.save(serie);
    }

    private void buscaEpisodioSerie(String nomeSerie){

        List<Episodio> episodios;
        Optional<Serie> serie = seriePresentOnList(nomeSerie);

        if(serie.isPresent()){
            System.out.println("Buscando episodios no banco. Serie id: " + serie.get().getId());
            episodios = episodioRepository.findBySerieId(serie.get().getId());

            if(episodios.isEmpty()){
                System.out.println("Buscando episodio da serie pois ainda nao temos");
                getEpisodesAndSave(serie.get(), nomeSerie);
            } else {
                System.out.println("Episodio encontrado para serie");
                episodios.forEach(System.out::println);
            }
        } else {
            System.out.println("Ainda nao temos informações sobre esta série. Obtendo mais informações...");

            // busca e adiciona serie ao banco
            serie = Optional.ofNullable(buscarSerie(getUriSerie(nomeSerie)));
            serie.ifPresent(value -> getEpisodesAndSave(value, nomeSerie));
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

    // FUNÇÕES FILMES
    private void buscarFilme(String uri){
        api.obterDadosFilme(uri);
    }

    // FUNÇÕES UTILIDADES
    private String buildUri(String nomePrograma){
        // Trata os espaços digitados pelo usuario
        String OMDB_URI = "http://www.omdbapi.com/?";
        return OMDB_URI + "t=" + nomePrograma.replace(" ", "+") + API_KEY;
    }

    private void limpaConsole(){
        //TODO: Fazer funcionar
        System.out.println("não esta funcionando");
    }
}