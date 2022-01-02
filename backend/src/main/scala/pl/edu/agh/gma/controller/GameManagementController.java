package pl.edu.agh.gma.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.dto.ConfigurationGameDto;
import pl.edu.agh.gma.dto.CreateNewGameRequest;
import pl.edu.agh.gma.dto.GameInfoResponse;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.service.AdminAuthService;
import pl.edu.agh.gma.service.game.GameService;

@CrossOrigin("*")
@RestController
@RequestMapping("sing/api/v1/admin/game")
@AllArgsConstructor
@Slf4j
public class GameManagementController {

  private final GameService gameService;
  private final AdminAuthService adminAuthService;

  @PostMapping("/new")
  public ResponseEntity<GameInfoResponse> createNewGame(@RequestBody CreateNewGameRequest newGameRequest,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    GameInfoResponse response = gameService.createNewGame(newGameRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/configuration/{gameSessionToken}")
  public ResponseEntity<ConfigurationGameDto> getGameConfigurationFor(@PathVariable String gameSessionToken,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    return gameService.getGameConfiguration(new GameSessionToken(gameSessionToken))
        .map(gameConf -> ResponseEntity.status(HttpStatus.OK).body(gameConf))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @PutMapping("/configuration/{gameSessionToken}")
  public ResponseEntity<ConfigurationGameDto> configureGame(@PathVariable String gameSessionToken,
      @RequestBody ConfigurationGameDto gameConfigurationDto, @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.status(HttpStatus.OK).body(gameService.configure(gameConfigurationDto).get());
  }

  @DeleteMapping("/configuration/{gameSessionToken}")
  public ResponseEntity<Boolean> deleteGameConfiguration(@PathVariable String gameSessionToken,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(gameService.deleteGameConfigurationFor(new GameSessionToken(gameSessionToken)));
  }

  @GetMapping("/activate/{gameSessionToken}")
  public ResponseEntity<GameInfoResponse> activateAndStartGame(@PathVariable String gameSessionToken,
      @RequestHeader(Constants.AUTH_HEADER) String bearer) {
    if (adminAuthService.isBearerInvalid(bearer)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    return gameService.activateAndStartGame(new GameSessionToken(gameSessionToken))
        .map(gameDetails -> ResponseEntity.status(HttpStatus.OK).body(gameDetails))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
  }

}