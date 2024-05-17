package io.github.matheusfy.screanmatch.model.repository;

import io.github.matheusfy.screanmatch.model.entity.Serie;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloIgnoreCase(String titulo);

    List<Serie> findByCategoria(Categoria categoria);
}
