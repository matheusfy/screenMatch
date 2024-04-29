package io.github.matheusfy.screanmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerieDTO(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") String ano,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("imdbRating") Double avaliacao,
        @JsonAlias("imdbVotes") String votos,
        @JsonAlias("Rated") String rate,
        @JsonAlias("totalSeasons") Integer totalTemporadas
) {

    @Override
    public String toString() {

        String saida = """
                Titulo: %s
                Ano: %s
                Duração: %s
                Genero: %s
                Avaliação: %.2f
                Votos: %s
                Total de temporadas: %d
                """.formatted(titulo,ano,duracao,genero,avaliacao,votos,totalTemporadas);
        return saida;
    }
}