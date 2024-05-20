package io.github.matheusfy.screanmatch.model.enums;

public enum Categoria {
    ACAO("Action", "Ação"),
    DRAMA("Drama", "Drama"),
    FANTASIA("Fantasy", "Fantasia"),
    TERROR("Horror", "Terror"),
    AVENTURA("Adventure", "Aventura"),
    COMEDIA("Comedy", "Comédia"),
    CRIME("Crime", "Crime");

    private final String categoriaOmdb;
    private final String categoriaOmdbPortugues;

    Categoria(String categoriaOmdb, String categoriaOmdbPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaOmdbPortugues = categoriaOmdbPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text) || categoria.toString().equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria não mapeada para a string fornecida: " + text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdbPortugues.equalsIgnoreCase(text)) {
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
