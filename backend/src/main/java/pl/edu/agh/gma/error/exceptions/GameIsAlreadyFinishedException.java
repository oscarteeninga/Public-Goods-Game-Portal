package pl.edu.agh.gma.error.exceptions;

public class GameIsAlreadyFinishedException extends RuntimeException {

  public GameIsAlreadyFinishedException(String gameSessionToken) {
    super("Game with token: " + gameSessionToken + " is already finished.");
  }

  public GameIsAlreadyFinishedException(String message, Exception exception) {
    super(message, exception);
  }
}
