package io.github.matheusfy.screanmatch.application;

import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.entity.Serie;
import io.github.matheusfy.screanmatch.model.api.ConsumoApi;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import io.github.matheusfy.screanmatch.model.repository.SerieRepository;

import java.util.*;

import static io.github.matheusfy.screanmatch.model.enums.Categoria.listarCategoria;

public class Principal {

    private final Scanner leitor;
    private final ConsumoApi api = new ConsumoApi();
    private final SerieRepository serieRepository;
    private String API_KEY = "&apikey=%s";

    public Principal(SerieRepository serieRepository){
        this.serieRepository = serieRepository;
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
                    if (!seriePresentOnList(nomeSerie)){
                        buscarSerie(getUriSerie(nomeSerie));
                    }
                }
                case "2" ->{
                    String nomeSerie = getNomeSerie();
                    buscaEpisodioSerie(getUriSerie(nomeSerie));
                }

                case "3" -> listarSeriesBuscadas();
                case "4" -> buscarSeriesCategoria();
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

    private boolean seriePresentOnList(String titulo){

        Optional<Serie> serie = serieRepository.findByTituloIgnoreCase(titulo.trim());
        if (serie.isPresent()){
            System.out.println(serie.get());
            return true;
        } else{
            return false;
        }
    }

    private String getNomeSerie(){
        System.out.println("Digite o nome da série: ");
        return leitor.nextLine();
    }

    private String getUriSerie(String nome){
        return buildUri(nome);
    }

    private void buscarSerie(String uri){

        Optional<SerieDTO> serieDTO = Optional.empty();
        try{
            serieDTO = api.obterDadosSerie(uri);
        } catch (RuntimeException error){
            System.out.println("Erro na conversão dos dados." + error.getMessage());
        }

        if(serieDTO.isPresent()){
            System.out.println("Informação serie: " + serieDTO.get());

            Serie serie = new Serie(serieDTO.get());

            //Adiciona no banco
            serieRepository.save(serie);
        }
    }

    private void buscaEpisodioSerie(String uri){
        api.obterEpisodiosSerie(uri);
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

}