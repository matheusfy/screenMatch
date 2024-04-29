package io.github.matheusfy.screanmatch.application;

import io.github.matheusfy.screanmatch.service.ConsumoApi;

import java.util.Scanner;

public class Principal {

    private Scanner leitor = new Scanner(System.in);
    private final String URI_API = "http://www.omdbapi.com/?";
    private final String API_KEY = "&apikey=35dcfa5c";
    private ConsumoApi api;

    public Principal(){
        this.api = new ConsumoApi();
    }

    public void exibeMenu(){
        System.out.println("Digite o nome da série/filme: ");
        String serie = leitor.nextLine();

        String uri = URI_API + "t=" + serie.replace(" ", "+") + API_KEY;
        api.obterDado(uri);
    }
}
