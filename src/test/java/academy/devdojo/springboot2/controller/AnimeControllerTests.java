package academy.devdojo.springboot2.controller;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;

@ExtendWith(SpringExtension.class)
public class AnimeControllerTests {
  
  @InjectMocks
  private AnimeController animeController;

  @Mock
  private AnimeService animeServiceMock;

  @BeforeEach
  void setup() {
    // mocking the method animeService.listAll
    PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
    BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
      .thenReturn(animePage);

  }

  @Test
  @DisplayName("List returns list of anime inside page object when successful")
  void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
    String expectedName = AnimeCreator.createValidAnime().getName();

    // Beginning the test of the method list of the controller
    Page<Anime> animePage = animeController.list(null).getBody();

    Assertions.assertThat(animePage).isNotNull();
    
    Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

  }
}
