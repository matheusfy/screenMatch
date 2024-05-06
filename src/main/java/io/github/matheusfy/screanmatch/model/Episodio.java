package io.github.matheusfy.screanmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.github.matheusfy.screanmatch.model.dtos.EpisodioDTO;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;

    private String titulo;
    private Integer  episodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer temporada, EpisodioDTO episodio){
        this.temporada = temporada;
        this.titulo = episodio.titulo();
        this.episodio = episodio.numero();

        try {
            this.avaliacao = Double.valueOf(episodio.avaliacao());
        } catch (NumberFormatException error){
            this.avaliacao = 0.0;
        }

        try{
            this.dataLancamento = LocalDate.parse(episodio.dataLancamento());
        } catch (DateTimeParseException error) {
            this.dataLancamento = null;
        }

    }

    public String getTitulo() {
        return this.titulo;
    }

    public Integer getTemporada(){
        return this.temporada;
    }

    public Double getAvaliacao(){
        return this.avaliacao;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", episodio=" + episodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
    
}
