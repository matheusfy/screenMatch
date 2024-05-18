package io.github.matheusfy.screanmatch.model.enums;

public enum Categoria {
    ACAO("Action"),
    DRAMA("Drama"),
    FANTASIA("Fantasy"),
    TERROR("Horror"),
    AVENTURA("Adventure"),
    COMEDIA("Comedy"),
    CRIME("Crime");

    private final String categoriaOmdb;
    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text) || categoria.toString().equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static void listarCategoria(){
        for(Categoria categoria: Categoria.values()){
            System.out.println(categoria);
        }
    }
}
