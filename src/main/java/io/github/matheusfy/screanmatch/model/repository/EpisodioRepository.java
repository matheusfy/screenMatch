package io.github.matheusfy.screanmatch.model.repository;

import io.github.matheusfy.screanmatch.model.entity.Episodio;
import io.github.matheusfy.screanmatch.model.entity.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EpisodioRepository extends JpaRepository<Episodio, Long> {

    public List<Episodio> findBySerieId(Long serieId);

    @Query(value = "select e from Serie s join s.episodios e where e.titulo ILIKE %?1%")
    public List<Episodio> findEpisodioPorTrecho(String trecho);

    @Query(value = "select e from Serie as s join s.episodios as e where s.titulo ilike %:nomeSerie% order by e.avaliacao desc limit 5")
    public List<Episodio> findTop5BySerieOrderByAvaliacaoDesc(String nomeSerie);
}
