package io.github.matheusfy.screanmatch;

import io.github.matheusfy.screanmatch.application.Principal;
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
        Principal menu = new Principal();
        menu.exibeMenu();
    }
}
