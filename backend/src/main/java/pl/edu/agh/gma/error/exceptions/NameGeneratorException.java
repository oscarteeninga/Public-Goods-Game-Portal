package pl.edu.agh.gma.error.exceptions;

public class NameGeneratorException extends RuntimeException {

  public NameGeneratorException(String message) {
    super(message);
  }

  public NameGeneratorException(String message, Exception exception) {
    super(message, exception);
  }
}
