package io.github.matheusfy.screanmatch.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieDTO(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") String ano,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("imdbRating") Double avaliacao,
        @JsonAlias("imdbVotes") String votos,
        @JsonAlias("Rated") String rate,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Plot") String snopse,
        @JsonAlias("Actors") String atores,
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
                Total de temporadas: %s
                """.formatted(titulo,ano,duracao,genero,avaliacao,votos,totalTemporadas);
        return saida;
    }
}
