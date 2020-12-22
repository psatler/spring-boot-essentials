package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

/**
 * Utility class for Anime
 */
public class AnimeCreator {
  
  public static Anime createAnimeToBeSaved() {
    return Anime.builder()
            .name("Dragon Ball Z")
            .build();
  }

  public static Anime createValidAnime() {
    return Anime.builder()
            .name("Dragon Ball Z")
            .id(1L)
            .build();
  }

  public static Anime createValidUpdatedAnime() {
    return Anime.builder()
            .name("Dragon Ball Z 222")
            .id(1L)
            .build();
  }
}
