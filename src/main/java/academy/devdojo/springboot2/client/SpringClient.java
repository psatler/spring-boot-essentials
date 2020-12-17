package academy.devdojo.springboot2.client;

import java.util.Arrays;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class SpringClient {
  
  public static void main(String[] args) {
    String url = "http://localhost:8080/animes/2";
    String urlWithPlaceholder = "http://localhost:8080/animes/{id}";

    String urlListOfAnimes = "http://localhost:8080/animes/all";

    // returns the whole response with status code, etc
    ResponseEntity<Anime> entity = new RestTemplate().getForEntity(url, Anime.class);

    log.info(entity);

    // returns only the object/body of the response
    Anime object = new RestTemplate().getForObject(url, Anime.class);
    log.info(object);

    // using placeholder variables instead of inserting them directly in the URL
    Anime object2 = new RestTemplate().getForObject(urlWithPlaceholder, Anime.class, 2);
    log.info(object2);

    // the way shown below we are returning an Array of animes
    Anime[] animes = new RestTemplate().getForObject(urlListOfAnimes, Anime[].class);
    log.info(Arrays.toString(animes));

    // returning a list of animes
    ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(urlListOfAnimes, HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Anime>>(){
      });
    log.info(exchange.getBody());


    // POST
    Anime kingdom = Anime.builder().name("kingdom").build();
    Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes/",kingdom, Anime.class);
    log.info("Saved animed {}", kingdomSaved);
    
    Anime samurai = Anime.builder().name("samurai").build();
    ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange(
      "http://localhost:8080/animes/",
      HttpMethod.POST,
      new HttpEntity<>(samurai, createHttpHeaders()),  
      Anime.class
    );
    log.info("Saved animed {}", samuraiSaved);
  }

  private static HttpHeaders createHttpHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    // httpHeaders.setBearerAuth(token);
    return httpHeaders;
  }
}
