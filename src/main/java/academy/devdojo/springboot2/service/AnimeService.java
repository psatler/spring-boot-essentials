package academy.devdojo.springboot2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeService {
  private final AnimeRepository animeRepository;
  
  public List<Anime> listAll() {
    return animeRepository.findAll();
  }

  public Anime findByIdOrThrowBadRequestError(long id) {
    return animeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
  }

  public Anime save(AnimePostRequestBody animePostRequestBody) {
    // Anime anime = Anime.builder().name(animePostRequestBody.getName()).build();
    Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);

    return animeRepository.save(anime);
  }

  public void delete(long id) {
    animeRepository.delete(findByIdOrThrowBadRequestError(id)); // using the other method above to find the object in the database first before deleting
  }

  public void replace(AnimePutRequestBody animePutRequestBody) {
    // Anime savedAnime = findByIdOrThrowBadRequestError(animePutRequestBody.getId());
    // Anime anime = Anime.builder()
    //                 .id(savedAnime.getId())
    //                 .name(animePutRequestBody.getName())
    //               .build();

    Anime savedAnime = findByIdOrThrowBadRequestError(animePutRequestBody.getId()); // making sure the element we are updating exists in the database first
    Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
    anime.setId(savedAnime.getId());
              
    animeRepository.save(anime);

  }

}
