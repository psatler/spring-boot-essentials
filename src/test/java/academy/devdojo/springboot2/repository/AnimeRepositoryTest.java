package academy.devdojo.springboot2.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

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
  @DisplayName("Save persists anime when successfull")
  void save_PersistAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();

    Anime animeSaved = this.animeRepository.save(animeToBeSaved);
    
    // using assertj for the assertions
    Assertions.assertThat(animeSaved).isNotNull();

    Assertions.assertThat(animeSaved.getId()).isNotNull();

    Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
  }
  
  @Test
  @DisplayName("Save updates anime when successfull")
  void save_UpdatesAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();

    Anime animeSaved = this.animeRepository.save(animeToBeSaved);

    animeSaved.setName("Overlord");

    Anime animeUpdated  = this.animeRepository.save(animeSaved);
    
    // using assertj for the assertions
    Assertions.assertThat(animeUpdated).isNotNull();
    Assertions.assertThat(animeUpdated.getId()).isNotNull();
    Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName()); // animeSaves already has the name "Overlord" at this point
  }
  
  @Test
  @DisplayName("Delete removes anime when successfull")
  void delete_RemovesAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();

    Anime animeSaved = this.animeRepository.save(animeToBeSaved);

    this.animeRepository.delete(animeSaved);

    Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

    Assertions.assertThat(animeOptional).isEmpty(); // Optional assert
  }
  
  @Test
  @DisplayName("Find by name returns list of anime when successfull")
  void findByName_ReturnsListOfAnime_WhenSuccessfull() {
    Anime animeToBeSaved = createAnime();

    Anime animeSaved = this.animeRepository.save(animeToBeSaved);

    String name = animeSaved.getName();

    List<Anime> animes = this.animeRepository.findByName(name);

    Assertions.assertThat(animes)
            .isNotEmpty()
            .contains(animeSaved);

  }

  @Test
  @DisplayName("Find by name returns empty list when no anime is found")
  void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
    List<Anime> animes = this.animeRepository.findByName("Not found name");

    Assertions.assertThat(animes).isEmpty();
  }

  @Test
  @DisplayName("Save throws ConstraintViolationException when name is empty")
  void save_ThrowConstraintViolationException_WhenNameIsEmpty() {
    Anime anime = new Anime();

    // first way of asserting the exception
    // Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
    //   .isInstanceOf(ConstraintViolationException.class);

    // another way of asserting the exception
    Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
      .isThrownBy(() -> this.animeRepository.save(anime))
      .withMessageContaining("The anime name cannot be empty");

  }
  

  private Anime createAnime() {
    return Anime.builder()
            .name("Dragon Ball Z")
            .build();
  }
}
