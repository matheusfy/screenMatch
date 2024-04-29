package io.github.matheusfy.screanmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodioDTO(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode") String numero,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("Released") String dataLancamento
){


}
