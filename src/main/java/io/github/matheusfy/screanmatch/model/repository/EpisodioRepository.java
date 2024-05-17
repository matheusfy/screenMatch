package io.github.matheusfy.screanmatch.model.repository;

import io.github.matheusfy.screanmatch.model.entity.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodioRepository extends JpaRepository<Episodio, Long> {

    public List<Episodio> findBySerieId(Long serieId);
}
