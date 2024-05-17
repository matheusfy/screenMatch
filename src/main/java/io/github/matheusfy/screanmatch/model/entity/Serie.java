package io.github.matheusfy.screanmatch.model.entity;

import io.github.matheusfy.screanmatch.model.api.OpenAiApi;
import io.github.matheusfy.screanmatch.model.dtos.SerieDTO;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;


@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String titulo;
    private String ano;
    private String duracao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private Double avaliacao;
    private String votos;
    private String rate;
    private Integer totalTemporadas;
    private String poster;
    private String atores;
    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie(SerieDTO serie){
        this.titulo             = serie.titulo();
        this.ano                = serie.ano();
        this.duracao        = serie.duracao();

        for(String categoria: serie.genero().split(",")) {
            try {
                this.categoria = Categoria.fromString(categoria.trim());
                break;
            } catch (Exception error){
                //TODO: Logar como warning para gerar uma tarefa para mapear a categoria. *nada critico*
                System.out.println("Categoria não mapeada: " + categoria);
             }
        }

        this.avaliacao      = OptionalDouble.of(serie.avaliacao()).orElse(0.0);
        this.votos              = serie.votos();
        this.rate                = serie.rate();
        this.poster            = serie.poster();
        this.sinopse = OpenAiApi.obterTraducao(serie.sinopse());
        this.atores            = serie.atores();
        this.totalTemporadas = serie.totalTemporadas();
    }

    public Serie() {

    }

    //*************** Init Getter and setters **********************//


    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(episodio -> episodio.setSerie(this));
        this.episodios = episodios;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
        return sinopse;
    }

    public void setSnopse(String snopse) {
        this.sinopse = snopse;
    }

    //***************** End Getters and Setters *********************
    @Override
    public String toString() {
        return "titulo='" + titulo + '\'' +
            ", ano='" + ano + '\'' +
            ", duracao='" + duracao + '\'' +
            ", categoria=" + categoria +
            ", avaliacao=" + avaliacao +
            ", totalTemporadas=" + totalTemporadas +
            ", sinopse='" + sinopse + '\'';
    }

}
