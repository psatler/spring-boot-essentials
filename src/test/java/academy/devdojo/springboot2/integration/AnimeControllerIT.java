package academy.devdojo.springboot2.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.DevDojoUser;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.repository.DevDojoRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
  @Autowired
  @Qualifier(value = "testRestTemplateRoleUser") // injecting the Bean created below
  private TestRestTemplate testRestTemplateRoleUser;
  
  @Autowired
  @Qualifier(value = "testRestTemplateRoleAdmin") // injecting the Bean created below
  private TestRestTemplate testRestTemplateRoleAdmin;

  @Autowired
  private AnimeRepository animeRepository;

  @Autowired
  private DevDojoRepository devDojoRepository;

  // @LocalServerPort
  // private int port;

  private static final DevDojoUser USER = DevDojoUser.builder()
                  .name("Dev Dojo")
                  .username("devdojo")
                  .password("{bcrypt}$2a$10$a5.cNRnxng6KDN52.fj.veZQcj.imiiqu01MK/Rilt6yKtP7.Jpc2")
                  .authorities("ROLE_USER")
                .build();

  private static final DevDojoUser ADMIN = DevDojoUser.builder()
                  .name("Admin Dev Dojo")
                  .username("admin")
                  .password("{bcrypt}$2a$10$a5.cNRnxng6KDN52.fj.veZQcj.imiiqu01MK/Rilt6yKtP7.Jpc2")
                  .authorities("ROLE_USER,ROLE_ADMIN")
                .build();


  @TestConfiguration
  @Lazy // wait a little bit before setting up the configurations
  static class Config {
    @Bean(name = "testRestTemplateRoleUser")
    public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
      // creating a new user and password in the in memory database for testing
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                      .rootUri("http://localhost:" + port)
                      .basicAuthentication("devdojo", "devdojo-pass");

      return new TestRestTemplate(restTemplateBuilder);
    }
    
    @Bean(name = "testRestTemplateRoleAdmin")
    public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
      // creating a new user and password in the in memory database for testing
      RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                      .rootUri("http://localhost:" + port)
                      .basicAuthentication("admin", "devdojo-pass");

      return new TestRestTemplate(restTemplateBuilder);
    }
  }


  @Test
  @DisplayName("list returns list of anime inside page object when successful")
  void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
    // using the actual repository to create the anime
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

    // creating a user in the in memory database
    devDojoRepository.save(USER);

    String expectedName = savedAnime.getName();

    // PageableResponse is a wrapper created for the PageImpl class
    PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null, 
      new ParameterizedTypeReference<PageableResponse<Anime>>(){
      }).getBody();

    Assertions.assertThat(animePage).isNotNull();
    
    Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

  }


  @Test
  @DisplayName("listAll returns list of anime when successful")
  void listAll_ReturnsListOfAnimes_WhenSuccessful() {
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(USER);

    String expectedName = savedAnime.getName();

    List<Anime> animePage = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Anime>>(){
      }).getBody();

    Assertions.assertThat(animePage)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animePage.get(0).getName()).isEqualTo(expectedName);
  }
  
  @Test
  @DisplayName("findById returns anime when successful")
  void findById_ReturnsAnime_WhenSuccessful() {
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(USER);

    Long expectedId = savedAnime.getId();

    Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expectedId);

    Assertions.assertThat(anime)
                .isNotNull();

    Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);

  }
  
  @Test
  @DisplayName("findByName returns a list of anime when successful")
  void findByName_ReturnsAListOfAnime_WhenSuccessful() {   
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(USER);

    String expectedName = savedAnime.getName();
    
    String url = String.format("/animes/find?name=%s", expectedName);
    
    List<Anime> animes = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Anime>>(){
      }).getBody();

    Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

    Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

  }
  
  @Test
  @DisplayName("findByName returns an empty list of anime when anime is not found")
  void findByName_ReturnsAnEmptyList_WhenAnimeIsNotFound() {
    devDojoRepository.save(USER);
    
    List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=dbz", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Anime>>(){
      }).getBody();

    Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

  }

  @Test
  @DisplayName("save returns anime when successful")
  void save_ReturnsAnime_WhenSuccessful() {
    AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
    devDojoRepository.save(USER);

    ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);

    Assertions.assertThat(animeResponseEntity).isNotNull();
    
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    
    Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
  }
  
  @Test
  @DisplayName("replace updates anime when successful")
  void replace_UpdatesAnime_WhenSuccessful() {
    // first, creating an Anime in the database
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(USER);

    // setting a new name so that we update the anime aftewards
    savedAnime.setName("new name");
    
    ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
      "/animes",
      HttpMethod.PUT,
      new HttpEntity<>(savedAnime),
      Void.class
    );

    Assertions.assertThat(animeResponseEntity).isNotNull();
    
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

  }
  
  @Test
  @DisplayName("delete removes anime when successful")
  void delete_RemovesAnime_WhenSuccessful() {
    // first, creating an Anime in the database
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(ADMIN);
    
    // using testRestTemplateRoleAdmin to perform request
    ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
      "/animes/admin/{id}",
      HttpMethod.DELETE,
      null,
      Void.class,
      savedAnime.getId()
    );

    Assertions.assertThat(animeResponseEntity).isNotNull();
    
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
  
  @Test
  @DisplayName("delete returns 403 when user is not admin")
  void delete_Returns403_WhenUserIsNotAdmin() {
    // first, creating an Anime in the database
    Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
    devDojoRepository.save(USER);
    
    ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
      "/animes/admin/{id}",
      HttpMethod.DELETE,
      null,
      Void.class,
      savedAnime.getId()
    );

    Assertions.assertThat(animeResponseEntity).isNotNull();
    
    Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }
}
