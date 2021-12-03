package pl.edu.agh.gma.error.exceptions;

public class GameConfigurationException extends RuntimeException {

  public GameConfigurationException(String gameSessionToken) {
    super("Game with token: " + gameSessionToken + " coudn't be configured");
  }

  public GameConfigurationException(String message, Exception exception) {
    super(message, exception);
  }
}
