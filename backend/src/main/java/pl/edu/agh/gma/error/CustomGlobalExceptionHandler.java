package pl.edu.agh.gma.error;

import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.agh.gma.error.exceptions.GameConfigurationException;
import pl.edu.agh.gma.error.exceptions.GameCreationException;
import pl.edu.agh.gma.error.exceptions.GameNotExistsException;
import pl.edu.agh.gma.error.exceptions.UserConnectionException;

@ControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({GameCreationException.class, GameConfigurationException.class, GameNotExistsException.class})
  public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {

    log.error((ex.toString()));
    ex.printStackTrace(System.out);

    CustomErrorResponse errors = new CustomErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setMsg(ex.getMessage());
    errors.setStatus(HttpStatus.CONFLICT.value());

    errors.setStackTrace(Arrays.toString(ex.getStackTrace()));

    return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({UserConnectionException.class})
  public ResponseEntity<CustomErrorResponse> userConnectionException(Exception ex, WebRequest request) {

    log.error((ex.toString()));
    ex.printStackTrace(System.out);

    CustomErrorResponse errors = new CustomErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setMsg(ex.getMessage());
    errors.setStatus(HttpStatus.NOT_ACCEPTABLE.value());

    errors.setStackTrace(Arrays.toString(ex.getStackTrace()));

    return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
  }
}

