package pl.edu.agh.gma.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.dto.gameprogress.GameProgressResponse;
import pl.edu.agh.gma.dto.gameprogress.GameStateResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionOutcomeResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionRequest;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.service.game.GameService;

@CrossOrigin("*")
@RestController
@RequestMapping("sing/api/v1/play/game")
@AllArgsConstructor
@Slf4j
public class GameController {

  private final GameService gameService;

  @GetMapping("/progress/{gameSessionToken}")
  public ResponseEntity<GameProgressResponse> getGameProgressInfo(@PathVariable GameSessionToken gameSessionToken) {
    GameProgressResponse gameProgressResponse = gameService.getGameProgressResponse(gameSessionToken);
    log.debug(gameProgressResponse.toString());

    return ResponseEntity.status(HttpStatus.OK).body(gameProgressResponse);
  }

  @GetMapping("/state/{gameSessionToken}")
  public ResponseEntity<GameStateResponse> getGameState(@PathVariable GameSessionToken gameSessionToken) {
    GameStateResponse gameStateResponse = gameService.getGameStateResponse(gameSessionToken);
    log.debug(gameStateResponse.toString());

    return ResponseEntity.status(HttpStatus.OK).body(gameStateResponse);
  }

  @PutMapping("/decision/{gameSessionToken}")
  public ResponseEntity<PlayerDecisionOutcomeResponse> addPlayerDecision(
      @PathVariable GameSessionToken gameSessionToken, @RequestBody PlayerDecisionRequest playerDecisionRequest) {

    log.debug(String.valueOf(playerDecisionRequest));
    PlayerDecisionOutcomeResponse outcome = gameService.addPlayerDecision(gameSessionToken, playerDecisionRequest);

    return ResponseEntity.status(HttpStatus.OK).body(outcome);
  }

}