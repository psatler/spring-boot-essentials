package academy.devdojo.springboot2.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor // all final methods are injected in the constructor
public class AnimeController {
  private final DateUtil dateUtil;
  private final AnimeService animeService;
  
  @GetMapping
  public ResponseEntity<List<Anime>> list() {
    log.info("Testing");
    log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

    return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Anime> findById(@PathVariable long id) {
    return ResponseEntity.ok(animeService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Anime> save(@RequestBody Anime anime) {
    // Jackson already maps the json properties to the class Anime so that we don't need to set the name in the animeService
    return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
  }
}
