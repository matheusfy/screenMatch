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
        @JsonAlias("Rated") String rate
) {

}
