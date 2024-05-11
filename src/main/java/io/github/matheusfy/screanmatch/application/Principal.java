package io.github.matheusfy.screanmatch.application;

import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.service.ConsumoApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Principal {

    private final Scanner leitor;
    private final ConsumoApi api = new ConsumoApi();
    private final String URI_API = "http://www.omdbapi.com/?";
    private final String API_KEY = "&apikey=35dcfa5c";

    private List<SerieDTO> lstSeriesBuscadas = new ArrayList<>();


    public Principal(){
        this.leitor = new Scanner(System.in);
    }

    public void exibeMenu(){

        String opcao = "-1";
        while(!opcao.equals("0")){

            System.out.println("1 - série \n2 - filme \n0 - Sair");
            opcao = leitor.nextLine();

            switch (opcao){
                case "1" ->{
                    exibeMenuSerie();
                }
                case "2" ->{

                    System.out.println("Digite o nome do filme: ");
                    buscarFilme(buildUri(leitor.nextLine()));
                }

                case "0" -> {
                    System.out.println("Saindo do menu principal");
                }
            }
        }
    }

    private void exibeMenuSerie(){

        String opcao = "-1";

        while(!opcao.equals("0")){
            System.out.println("1 - Buscar série \n2 - Buscar episódio \n 3 - Lista Séries buscadas\n0 - Sair");
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

                case "3" -> {
                    listarSeriesBuscadas();
                }

                case "0" -> {
                    System.out.println("Saindo do menu de série");
                }
            }
        }

    }

    // FUNÇÕES SÉRIES
    private void listarSeriesBuscadas(){
        lstSeriesBuscadas.forEach(System.out::println);
    }

    private boolean seriePresentOnList(String nomeSerie){
        Optional<SerieDTO> serieDTO = lstSeriesBuscadas.stream()
            .filter(serie -> serie.titulo().equals(nomeSerie))
            .findFirst();

        if(serieDTO.isPresent()){
            System.out.println("Serie encontrada na lista: " + serieDTO.toString());
            return true;
        } else {
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
        SerieDTO serie = api.obterDadosSerie(uri);
        System.out.println("Informação serie: " + serie.toString());

        if(!lstSeriesBuscadas.contains(serie)){
            lstSeriesBuscadas.add(serie);
        }
        else{
            // TODO: Retornar "serie já foi buscada"
            System.out.println("Serie já foi buscada");
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
        return URI_API + "t=" + nomePrograma.replace(" ", "+") + API_KEY;
    }
}