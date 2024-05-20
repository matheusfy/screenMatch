package io.github.matheusfy.screanmatch.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FilmeDTO(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") Integer ano,
        @JsonAlias("Released") String lancamento,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("imdbRating") Double avaliacao,
        @JsonAlias("imdbVotes") String votos
) {
    @Override
    public String toString() {
        String text = """
                Título: %s
                Ano: %d
                Lançamento: %s
                Duração: %s
                Gênero: %s
                Avaliação: %.2f
                Votos: %s
                """.formatted(titulo,ano,lancamento,duracao,genero,avaliacao,votos);
        return text;
    }
}
