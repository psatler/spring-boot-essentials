package academy.devdojo.springboot2.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import academy.devdojo.springboot2.domain.Anime;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
public class AnimeRepositoryTest {
  @Autowired
  private AnimeRepository animeRepository;
  
  @Test
  @DisplayName("Save creates anime when successfull")
  void save_PersistAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();
    Anime animeSaved = this.animeRepository.save(animeToBeSaved);
    
    // using assertj for the assertions
    Assertions.assertThat(animeSaved).isNotNull();
    Assertions.assertThat(animeSaved.getId()).isNotNull();
    Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
  }

  private Anime createAnime() {
    return Anime.builder()
            .name("Dragon Ball Z")
            .build();
  }
}
