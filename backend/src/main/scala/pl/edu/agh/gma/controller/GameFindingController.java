package pl.edu.agh.gma.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.dto.GameInfoResponse;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.service.AdminAuthService;
import pl.edu.agh.gma.service.game.GameService;

@CrossOrigin("*")
@RestController
@RequestMapping("sing/api/v1/admin/games/find")
@AllArgsConstructor
@Slf4j
public class GameFindingController {

  private final GameService gameService;
  private final AdminAuthService adminAuthService;

  @GetMapping("/all")
  public ResponseEntity<List<GameInfoResponse>> getAllGamesInfo(@RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    List<GameInfoResponse> gamesData = gameService.getAllGames();

    if (gamesData.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return ResponseEntity.status(HttpStatus.OK).body(gamesData);
  }

  @GetMapping("/name/{namepart}")
  public ResponseEntity<List<GameInfoResponse>> getAllGamesInfoWithName(@PathVariable String namepart,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    List<GameInfoResponse> gamesData = gameService.getAllGamesWithName(namepart);

    if (gamesData.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return ResponseEntity.status(HttpStatus.OK).body(gamesData);
  }

  @GetMapping("/text/{text}")
  public ResponseEntity<List<GameInfoResponse>> getAllGamesInfoWitText(@PathVariable String text,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    // looks for any reference of text in all games parameters (gameName, description, etc.)
    List<GameInfoResponse> gamesData = gameService.getAllGamesWithText(text);

    if (gamesData.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return ResponseEntity.status(HttpStatus.OK).body(gamesData);
  }

  @GetMapping("/{gameSessionToken}")
  public ResponseEntity<GameInfoResponse> getGameInfo(@PathVariable String gameSessionToken,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return gameService.getGame(new GameSessionToken(gameSessionToken))
        .map(gameInfoResponse -> ResponseEntity.status(HttpStatus.OK).body(gameInfoResponse))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

}
