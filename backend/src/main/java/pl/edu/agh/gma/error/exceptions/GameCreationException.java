package pl.edu.agh.gma.error.exceptions;

public class GameCreationException extends RuntimeException {

  public GameCreationException(String gameName) {
    super("Game with name: " + gameName + " is already taken!");
  }

  public GameCreationException(String message, Exception exception) {
    super(message, exception);
  }
}

