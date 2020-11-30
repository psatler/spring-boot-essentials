package academy.devdojo.springboot2.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
  private final DateUtil dateUtil;
  
  @GetMapping(path = "list")
  public List<Anime> list() {
    log.info("Testing");
    log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));

    return List.of(new Anime("Test1"), new Anime("Test2"));
  }
}
