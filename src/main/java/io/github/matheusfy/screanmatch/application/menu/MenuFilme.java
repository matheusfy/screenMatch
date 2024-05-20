package io.github.matheusfy.screanmatch.application.menu;

import io.github.matheusfy.screanmatch.model.api.ConsumoApi;
import io.github.matheusfy.screanmatch.model.dtos.FilmeDTO;
import io.github.matheusfy.screanmatch.model.entity.Filme;
import io.github.matheusfy.screanmatch.model.repository.FilmeRepository;

import java.util.List;
import java.util.Scanner;

import static io.github.matheusfy.screanmatch.application.Principal.buildUri;

public class MenuFilme {

    private Scanner cmd;
    private ConsumoApi api;
    private FilmeRepository repository;

    public MenuFilme(FilmeRepository filmeRepository, Scanner cmd, ConsumoApi api) {
        this.cmd = cmd;
        this.api = api;
        this.repository = filmeRepository;
    }

    public void exibeMenu(){

        String opcao = "-1";
        String menu = """
            1 - Buscar filme  na web\s
            2 - Listar filmes buscados\s
            0 - Sair
            """;
        while(!opcao.equals("0")){
            System.out.println(menu);
            opcao = cmd.nextLine();

            switch (opcao){

                case "1" -> {
                    System.out.println("Digite o nome do filme: ");
                    buscarFilme(buildUri(cmd.nextLine()));
                }
                case "2" -> {
                    listarFilmes();
                }
                case "0" -> System.out.println("Saindo do menu de série");
            }
        }
    }

    // FUNÇÕES FILMES
    private void buscarFilme(String uri){
        try{
            FilmeDTO filmeDTO = api.obterDadosFilme(uri);
            if(filmeDTO != null){
                Filme filme = new Filme(filmeDTO);
                System.out.println(filme);
                try {
                    repository.save(filme);
                } catch (Exception erro){
                    System.out.println(erro.getMessage());
                }
            }
        } catch (Exception error) {
            System.out.println("Exception: " + error.getMessage());
        }
    }

    private void listarFilmes() {
        List<Filme> filmes = repository.findAll();
        if(!filmes.isEmpty()){
            filmes.forEach(System.out::println);
        } else {
            System.out.println("Lista de filmes vazia");
        }
    }
}
