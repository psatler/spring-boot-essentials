package academy.devdojo.springboot2.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AnimeControllerIT {
  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private AnimeRepository animeRepository;

  @LocalServerPort
  private int port;



  @Test
  @DisplayName("list returns list of anime inside page object when successful")
  void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
    // using the actual repository to create the anime
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

    String expectedName = savedAnime.getName();

    // PageableResponse is a wrapper created for the PageImpl class
    PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null, 
      new ParameterizedTypeReference<PageableResponse<Anime>>(){
      }).getBody();

    Assertions.assertThat(animePage).isNotNull();
    
    Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

  }
}
