package io.github.matheusfy.screanmatch;

import io.github.matheusfy.screanmatch.service.ConsumoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreanMatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreanMatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConsumoApi api = new ConsumoApi();
        String endpoint = "http://www.omdbapi.com/?apikey=35dcfa5c";
        String title = "supernatural";
        if (title != "")
        {
            endpoint = endpoint + "&t=" + title;
        }

        System.out.println(api.obterDado(endpoint));
    }
}
