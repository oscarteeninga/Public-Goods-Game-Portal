package pl.edu.agh.gma.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.User;

/**
 * Stores information about players connected to each game
 */
@Component
@AllArgsConstructor
@Slf4j
public class SessionManager {

  /**
   * key = Game.sessionToken, value = List of Users for current game
   */
  // TODO for better performance List -> HashMap
  private final HashMap<GameSessionToken, List<User>> sessions;

  public void restartUserSessions(GameSessionToken gameSessionToken) {
    log.info("Restarting Game: " + gameSessionToken);
    sessions.put(gameSessionToken, new ArrayList<>());
  }

  public boolean connectUserToGame(User user, GameSessionToken gameSessionToken) {
    log.debug("Connecting to game $(" + gameSessionToken + ") user: " + user);

    if (isUserConnectedToGame(user, gameSessionToken)) {
      return false;
    }

    List<User> userList = sessions.getOrDefault(gameSessionToken, new ArrayList<>());
    userList.add(user);
    sessions.put(gameSessionToken, userList);
    return true;
  }

  public boolean isUserConnectedToGame(User user, GameSessionToken gameSessionToken) {
    log.trace("Game $(" + gameSessionToken + ") checking if connected user: " + user);
    if (!sessions.containsKey(gameSessionToken)){
      return false;
    }

    return sessions.get(gameSessionToken).contains(user);
  }

  public boolean isUserConnectedToGame(String username, GameSessionToken gameSessionToken) {
    User userByName = new User();
    userByName.setUsername(username);
    return isUserConnectedToGame(userByName, gameSessionToken);
  }

  public HashMap<GameSessionToken, List<User>> getAll() {
    return sessions;
  }

  public List<User> getUsersFor(GameSessionToken gameSessionToken) {
    return sessions.getOrDefault(gameSessionToken, new ArrayList<>());
  }

  public int getUsersNumFor(GameSessionToken gameSessionToken) {
    return sessions.containsKey(gameSessionToken) ? sessions.get(gameSessionToken).size() : 0;
  }

}
