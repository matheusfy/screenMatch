package io.github.matheusfy.screanmatch.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TemporadaDTO(
        @JsonAlias("Season")  Integer numero,
        @JsonAlias("Episodes")  List<EpisodioDTO> lstEpisodios

) {
}
