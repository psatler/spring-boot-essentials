package academy.devdojo.springboot2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import academy.devdojo.springboot2.domain.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long> {


}
