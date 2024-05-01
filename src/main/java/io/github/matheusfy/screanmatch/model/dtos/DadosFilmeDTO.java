package io.github.matheusfy.screanmatch.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosFilmeDTO(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") String ano,
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
                Ano: %s
                Lançamento: %s
                Duração: %s
                Gênero: %s
                Avaliação: %.2f
                Votos: %s
                """.formatted(titulo,ano,lancamento,duracao,genero,avaliacao,votos);
        return text;
    }
}
