package academy.devdojo.springboot2.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.exception.BadRequestExceptionDetails;

@ControllerAdvice // indicating this is a class all controllers should use
public class RestExceptionHandler {

  @ExceptionHandler(BadRequestException.class) // when there is an exception of the type BadRequest, execute the method below
  public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
    return new ResponseEntity<>(
      BadRequestExceptionDetails.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .title("Bad Request Exception. Check the documentation!")
        .details(bre.getMessage())
        .developerMessage(bre.getClass().getName())
        .build(),
        HttpStatus.BAD_REQUEST
    );
  }
  
}
