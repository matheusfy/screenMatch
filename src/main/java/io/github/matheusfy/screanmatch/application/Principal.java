package io.github.matheusfy.screanmatch.application;

import io.github.matheusfy.screanmatch.service.ConsumoApi;
import java.util.Scanner;


public class Principal {

    private final Scanner leitor;
    private final ConsumoApi api = new ConsumoApi();

    public Principal(){
        this.leitor = new Scanner(System.in);
    }

    public void exibeMenu(){
        System.out.println("Digite o nome da série/filme: ");
        String serie = leitor.nextLine();

        String URI_API = "http://www.omdbapi.com/?";
        String API_KEY = "&apikey=35dcfa5c";
        String uri = URI_API + "t=" + serie.replace(" ", "+") + API_KEY;

        api.obterDado(uri);

    }

}
