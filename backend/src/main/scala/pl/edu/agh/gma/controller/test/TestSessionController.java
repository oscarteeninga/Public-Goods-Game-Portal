package pl.edu.agh.gma.controller.test;

import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.util.SessionManager;

@RestController
@RequestMapping("test/session")
@AllArgsConstructor
public class TestSessionController {

  private final SessionManager sessionManager;

  /**
   * http://localhost:8080/test/session/restart?game=gametoken1
   * http://localhost:8080/test/session/restart?game=gametoken2
   * http://localhost:8080/test/session/connect?game=gametoken1&username=Kasia&usertoken=usertoken1
   * http://localhost:8080/test/session/connect?game=gametoken2&username=Andrzej&usertoken=usertoken2
   * http://localhost:8080/test/session/get
   * <p>
   * True:
   * http://localhost:8080/test/session/isconnected?game=gametoken1&username=Kasia&usertoken=usertoken1
   * False:
   * http://localhost:8080/test/session/isconnected?game=gametoken2&username=Kasia&usertoken=usertoken1
   */

  @PostMapping("/restart")
  public ResponseEntity<String> restartGame(@RequestParam(required = true, name = "game") String gameToken) {

    GameSessionToken gameSessionToken = new GameSessionToken(gameToken);

    sessionManager.restartUserSessions(gameSessionToken);

    return ResponseEntity.status(HttpStatus.OK).body("Restarted.");
  }

  @GetMapping("/get")
  public ResponseEntity<HashMap<GameSessionToken, List<User>>> getAll() {
    return ResponseEntity.status(HttpStatus.OK).body(sessionManager.getAll());
  }

  @PostMapping("/connect")
  public ResponseEntity<Boolean> connectUserToGame(@RequestParam(required = true, name = "username") String username,
      @RequestParam(required = true, name = "gameSession") String gameToken) {
    return ResponseEntity.status(HttpStatus.OK).body(sessionManager.connectUserToGame(new User(username, ""), new GameSessionToken(gameToken)));
  }

  @GetMapping("/isconnected")
  public ResponseEntity<Boolean> isUserConnectedToGame(
      @RequestParam(required = true, name = "username") String username,
      @RequestParam(required = true, name = "gameSession") String gameToken) {

    User _user = new User();
    if (username != null) {
      _user.setUsername(username);
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(sessionManager.isUserConnectedToGame(_user, new GameSessionToken(gameToken)));
  }
}
