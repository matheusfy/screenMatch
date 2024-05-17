package io.github.matheusfy.screanmatch.model.entity;

import io.github.matheusfy.screanmatch.model.dtos.EpisodioDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    @Column(unique = true, nullable = false)
    private String titulo;

    private Integer  episodio;
    private Integer temporada;
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

    public Episodio() {

    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
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


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
