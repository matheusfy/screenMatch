package io.github.matheusfy.screanmatch.model.repository;

import io.github.matheusfy.screanmatch.model.entity.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
}
