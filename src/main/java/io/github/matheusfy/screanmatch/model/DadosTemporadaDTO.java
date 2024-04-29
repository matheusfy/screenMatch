package io.github.matheusfy.screanmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporadaDTO(
        @JsonAlias("Season")  Integer numero,
        @JsonAlias("Episodes")  List<DadosEpisodioDTO> lstEpisodios

) {
}
