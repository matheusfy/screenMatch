package io.github.matheusfy.screanmatch.model.entity;

import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.enums.Categoria;

import java.util.OptionalDouble;

public class Serie {

    private String titulo;
    private String ano;
    private String duracao;
    private Categoria categoria;
    private Double avaliacao;
    private String votos;
    private String rate;
    private Integer totalTemporadas;
    private String poster;
    private String atores;
    private String snopse;

    public Serie(SerieDTO serie){
        this.titulo             = serie.titulo();
        this.ano                = serie.ano();
        this.duracao        = serie.duracao();
        this.categoria      = Categoria.fromString(serie.genero().split(",")[0].trim());
        this.avaliacao      = OptionalDouble.of(serie.avaliacao()).orElse(0.0);
        this.votos              = serie.votos();
        this.rate                = serie.rate();
        this.poster            = serie.poster();
        this.snopse           = serie.snopse();
        this.atores            = serie.atores();
        this.totalTemporadas = serie.totalTemporadas();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getVotos() {
        return votos;
    }

    public void setVotos(String votos) {
        this.votos = votos;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getSnopse() {
        return snopse;
    }

    public void setSnopse(String snopse) {
        this.snopse = snopse;
    }

    @Override
    public String toString() {
        return "titulo='" + titulo + '\'' +
            ", ano='" + ano + '\'' +
            ", duracao='" + duracao + '\'' +
            ", categoria=" + categoria +
            ", avaliacao=" + avaliacao +
            ", totalTemporadas=" + totalTemporadas +
            ", snopse='" + snopse + '\'';
    }
}
