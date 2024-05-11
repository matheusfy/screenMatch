package io.github.matheusfy.screanmatch.model.enums;

public enum Categoria {
    ACAO("Action"),
    DRAMA("Drama"),
    FANTASIA("Fantasy"),
    TERROR("Horror"),
    AVENTURA("Adventure"),
    COMEDIA("Comedy");

    private String categoriaOmdb;
    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }
}
