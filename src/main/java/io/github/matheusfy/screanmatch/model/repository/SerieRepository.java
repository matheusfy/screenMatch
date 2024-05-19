package io.github.matheusfy.screanmatch.model.repository;

import io.github.matheusfy.screanmatch.model.entity.Serie;
import io.github.matheusfy.screanmatch.model.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

	Optional<Serie> findByTituloIgnoreCase(String titulo);

	List<Serie> findByCategoria(Categoria categoria);

	List<Serie> findByTituloContainingIgnoreCase(String palavra);

	List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);

	List<Serie> findTop5ByOrderByAvaliacaoDesc();

	@Query(value = "select * from series where total_temporadas <= ?1 and avaliacao >= ?2", nativeQuery = true)
	List<Serie> findByTemporadaAndAvaliacao(Integer temporada, Double avaliacao);

}
