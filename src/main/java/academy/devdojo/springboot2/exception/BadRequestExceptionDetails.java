package academy.devdojo.springboot2.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadRequestExceptionDetails {
  private String title;
  private int status;
  private String details;
  private String developerMessage;
  private LocalDateTime timestamp;
}
