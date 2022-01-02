package pl.edu.agh.gma.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.game.GamesStateManager;
import pl.edu.agh.gma.service.util.SessionManager;

@Slf4j
@Service
@AllArgsConstructor
public class SessionService {

  private final SessionManager sessionManager;
  private final GamesStateManager gamesStateManager;
  private final UserManager userManager;

  public boolean connectUserToGame(User user, GameSessionToken gameSessionToken) {
    if (!gamesStateManager.isGameConfigured(gameSessionToken) ||
        !userManager.isUserRegisteredByName(user.getUsername()))
      return false;

    boolean res = sessionManager.connectUserToGame(user, gameSessionToken);
    gamesStateManager.updateConnectedUserNum(gameSessionToken, sessionManager.getUsersNumFor(gameSessionToken));
    return res;
  }

  public boolean isUserConnectedToGame(User user, GameSessionToken gameSessionToken) {
    return sessionManager.isUserConnectedToGame(user, gameSessionToken);
  }
}
