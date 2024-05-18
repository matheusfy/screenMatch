package io.github.matheusfy.screanmatch;

import io.github.matheusfy.screanmatch.application.Principal;
import io.github.matheusfy.screanmatch.model.repository.EpisodioRepository;
import io.github.matheusfy.screanmatch.model.repository.FilmeRepository;
import io.github.matheusfy.screanmatch.model.repository.SerieRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ScreanMatchApplication implements CommandLineRunner {

    private final SerieRepository serieRepository;
    private final EpisodioRepository episodioRepository;
    private final FilmeRepository filmeRepository;

    public ScreanMatchApplication(FilmeRepository filmeRepository, EpisodioRepository episodioRepository,
            SerieRepository serieRepository) {
        this.filmeRepository = filmeRepository;
        this.episodioRepository = episodioRepository;
        this.serieRepository = serieRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScreanMatchApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Principal menu = new Principal(serieRepository, episodioRepository, filmeRepository);
        menu.exibeMenu();
    }
}
