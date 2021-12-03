package pl.edu.agh.gma.error.exceptions;

import pl.edu.agh.gma.model.GameSessionToken;

public class UserConnectionException extends RuntimeException {

  public UserConnectionException(String username, GameSessionToken gameSessionToken) {
    super("User: " + username + " cannot be connected to: " + gameSessionToken);
  }

  public UserConnectionException(String message, Exception exception) {
    super(message, exception);
  }
}
