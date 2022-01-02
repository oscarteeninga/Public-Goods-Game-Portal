package pl.edu.agh.gma.controller.test;

import java.util.HashMap;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.dto.CreateNewGameRequest;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.service.game.GamesStateManager;
import pl.edu.agh.gma.service.util.GameState;

@RestController
@RequestMapping("test/GameManager")
@AllArgsConstructor
public class TestGameManagerController {

  private final GamesStateManager gamesStateManager;

  /**
   * http://localhost:8080/test/GameManager/createGame
   * http://localhost:8080/test/GameManager/getActiveGameBy?token=
   */

  @PostMapping("/createGame")
  public ResponseEntity<GameState> createNewGame(@RequestBody CreateNewGameRequest newGameRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(gamesStateManager.createGame(newGameRequest.getGameName(), newGameRequest.getDescription()));
  }

  @GetMapping("/getGameStateBy")
  public ResponseEntity<GameState> getGameStateBy(
      @RequestParam(required = true, name = "token") String gameSessionToken) {
    Optional<GameState> gameState = gamesStateManager.getGameStateBy(new GameSessionToken(gameSessionToken));

    return ResponseEntity.status(HttpStatus.OK).body(gameState.get());
  }

  @GetMapping("/isGameActive")
  public ResponseEntity<Boolean> isGameActive(@RequestParam(required = true, name = "token") String gameSessionToken) {
    Boolean isActive = gamesStateManager.isGameActive(new GameSessionToken(gameSessionToken));

    return ResponseEntity.status(HttpStatus.OK).body(isActive);
  }

  @GetMapping("/getAll")
  public ResponseEntity<HashMap<GameSessionToken, GameState>> getAll() {
    return ResponseEntity.status(HttpStatus.OK).body(gamesStateManager.getAll());
  }

}
