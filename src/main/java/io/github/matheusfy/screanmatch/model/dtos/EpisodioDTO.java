package io.github.matheusfy.screanmatch.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodioDTO(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode") Integer numero,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("Released") String dataLancamento
){

    @Override
    public String toString(){
        String text = """
                Titulo: %s,
                Episodio: %d,
                Avaliação: %s,
                Data Lançamento: %s
                """.formatted(titulo, numero,avaliacao, dataLancamento);

        return text;
    }

}
