package academy.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AnimePostRequestBody {

  @NotEmpty(message = "The anime name cannot be empty")
  private String name;  
}
