package io.github.matheusfy.screanmatch;

import io.github.matheusfy.screanmatch.application.Principal;
import io.github.matheusfy.screanmatch.model.repository.EpisodioRepository;
import io.github.matheusfy.screanmatch.model.repository.SerieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ScreanMatchApplication implements CommandLineRunner {

    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private EpisodioRepository episodioRepository;


    public static void main(String[] args) {
        SpringApplication.run(ScreanMatchApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Principal menu = new Principal(serieRepository, episodioRepository);
        menu.exibeMenu();
    }
}
