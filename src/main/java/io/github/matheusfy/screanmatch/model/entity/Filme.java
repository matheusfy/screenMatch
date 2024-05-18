package io.github.matheusfy.screanmatch.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "filmes" )
public class Filme {


    @Id
    private Long id;

    // TODO: Adicionar atributos para classe de filme


    public Filme(){

    }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
