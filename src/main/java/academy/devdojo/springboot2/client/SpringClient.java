package academy.devdojo.springboot2.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class SpringClient {
  
  public static void main(String[] args) {
    String url = "http://localhost:8080/animes/2";
    String urlWithPlaceholder = "http://localhost:8080/animes/{id}";

    // returns the whole response with status code, etc
    ResponseEntity<Anime> entity = new RestTemplate().getForEntity(url, Anime.class);

    log.info(entity);

    // returns only the object/body of the response
    Anime object = new RestTemplate().getForObject(url, Anime.class);
    log.info(object);

    // using placeholder variables instead of inserting them directly in the URL
    Anime object2 = new RestTemplate().getForObject(urlWithPlaceholder, Anime.class, 2);
    log.info(object2);
  }
}
