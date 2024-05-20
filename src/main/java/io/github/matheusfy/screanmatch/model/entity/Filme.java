package io.github.matheusfy.screanmatch.model.entity;

import io.github.matheusfy.screanmatch.model.dtos.FilmeDTO;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.github.matheusfy.screanmatch.service.Conversor.LocalDateToBrFormat;
import static io.github.matheusfy.screanmatch.service.Conversor.omdbDateToLocalDate;

@Entity
@Table(name = "filmes" )
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;
    private Integer ano;
    private LocalDate lancamento;
    private String duracao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private Double avaliacao;

    private Long votos;


    public Filme(){

    }

    public Filme(FilmeDTO filme) {

        this.titulo = filme.titulo();
        this.ano = filme.ano();

        try{
            // valor que recebemos tem o formato (dd MMM yyyy)
            this.lancamento = omdbDateToLocalDate(filme.lancamento());
        } catch (Exception error){
            System.out.println("Não foi possível converter a data: " + error.getMessage());
            this.lancamento = null;
        }

        this.duracao = filme.duracao();

        for(String categoria: filme.genero().split(",")){
            try{
                this.categoria = Categoria.fromString(categoria.trim());
            } catch (Exception error){
                System.out.println("Exception: "+ error.getMessage());
            }
        }

        this.avaliacao = filme.avaliacao();
        this.votos = Long.parseLong(filme.votos().replace(",", ""));
    }
    public void setId(@NotNull Long id) {
        this.id = id;
    }

    @NotNull
    public Long getId() {
        return id;
    }
    @NotNull
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NotNull String titulo) {
        this.titulo = titulo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public LocalDate getLancamento() {
        return lancamento;
    }

    public void setLancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    @Override
    public String toString() {
        return "titulo='" + titulo + '\'' +
            ", ano=" + ano +
            ", lancamento=" + LocalDateToBrFormat(lancamento)+
            ", duracao='" + duracao + '\'' +
            ", categoria='" + categoria.toString() + '\'' +
            ", avaliacao=" + avaliacao +
            ", votos='" + votos + '\'';
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Long getVotos() {
        return votos;
    }

    public void setVotos(Long votos) {
        this.votos = votos;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

}
