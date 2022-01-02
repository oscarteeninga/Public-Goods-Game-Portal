package pl.edu.agh.gma.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.dto.SessionRequest;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.user.SessionService;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("sing/api/v1/game/session")
@AllArgsConstructor
public class SessionController {

  private final SessionService sessionService;

  @GetMapping("/connect")
  public ResponseEntity<Boolean> connectUserToGame(@RequestParam(required = true, name = "username") String username,
      @RequestParam(required = true, name = "gameSession") String gameToken) {
    User _user = new User();
    if (username != null) {
      _user.setUsername(username);
    }
    log.info("connecting User: " + username);
    return ResponseEntity.status(HttpStatus.OK)
        .body(sessionService.connectUserToGame(_user, new GameSessionToken(gameToken)));
  }

  @PostMapping("/connect")
  public ResponseEntity<Boolean> connectUserToGame2(@RequestBody SessionRequest sessionRequest) throws Exception {
    // TODO
    throw new Exception("TODO implement for request body posting");
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
        .body(sessionService.isUserConnectedToGame(_user, new GameSessionToken(gameToken)));
  }
}
