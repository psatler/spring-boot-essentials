package academy.devdojo.springboot2.repository;

import java.util.List;

import academy.devdojo.springboot2.domain.Anime;

public interface AnimeRepository {
  List<Anime> listAll();
}
