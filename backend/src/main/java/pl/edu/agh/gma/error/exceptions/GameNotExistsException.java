package pl.edu.agh.gma.error.exceptions;

public class GameNotExistsException extends RuntimeException {

  public GameNotExistsException(String gameSessionToken) {
    super("Game with session token: " + gameSessionToken + " does not exists in memory!");
  }

  public GameNotExistsException(String message, Exception exception) {
    super(message, exception);
  }
}
