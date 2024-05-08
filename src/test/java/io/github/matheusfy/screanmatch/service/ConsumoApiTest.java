package io.github.matheusfy.screanmatch.service;

import io.github.matheusfy.screanmatch.model.Episodio;
import io.github.matheusfy.screanmatch.model.dtos.EpisodioDTO;
import io.github.matheusfy.screanmatch.model.dtos.TemporadaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsumoApiTest {

  private final ConsumoApi consumo = new ConsumoApi();

  @Mock
  List<Episodio>  mockEpisodios;

  @BeforeEach
  public void setup() {
    mockEpisodios = mock(List.class);
  }

  @Test
  void obterDado() {
    //TODO: Faz muita coisa. Deve ser refatorado.
  }

  @Test
  void getTemporadas() {
    // TODO: Esta função cria uma requisição e consulta a API do omdb e retorna a lista de temporadas caso a requisição for de uma série
  }

  @Test
  void getEpisodios() {
    // TODO: Criar uma lista de temporadas e retornar um lista de episodios
  }

  @Test
  void getMelhores5Episodios() {
    //TODO: Passa uma lista de temporadas e nela busca as 5 primeiros episodios
  }

  @Test
  public void buscaEpisodio() {

    Episodio episodio1 = new Episodio(1, new EpisodioDTO("Episodio 1", 1, "8.5", "01 Jan 2022"));
    Episodio episodio2 = new Episodio(1, new EpisodioDTO("Episodio 2", 2, "8.7", "08 Jan 2022"));
    Episodio episodio3 = new Episodio(1, new EpisodioDTO("Episodio 3", 3, "9.0", "15 Jan 2022"));

    when(mockEpisodios.stream()).thenReturn(Stream.of(episodio1, episodio2, episodio3));

    Optional<Episodio> resultado = consumo.buscaEpisodio("Episodio 2", mockEpisodios);

    assertEquals(episodio2, resultado.get());
    assertNotEquals(episodio1, resultado.get());
  }

  @Test
  void mostrarMediaTemporadas() {
    //TODO: Recebe uma lista de temporadas. Deve retornar a média das avaliações de cada temporada.
  }
}