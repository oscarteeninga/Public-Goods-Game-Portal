package pl.edu.agh.gma.service.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionOutcomeResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionRequest;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.GameConfiguration;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.RoundResultDao;
import pl.edu.agh.gma.service.TokenService;
import pl.edu.agh.gma.service.util.GameState;
import pl.edu.agh.gma.service.util.SessionManager;

/**
 * Stores information about all currently running games
 */
@Component
@Slf4j
@AllArgsConstructor
public class GamesStateManager {

  private final TokenService tokenService;

  private final RoundResultDao roundResultDao;

  private final SessionManager sessionManager;

  private final GameDao gameDao;

  private final GameConfigurationDao gameConfigurationDao;

  /**
   * key = gameSessionToken , value = GameState
   */
  private final HashMap<GameSessionToken, GameState> allGames;

  public GameState createGame(String gameName, String gameDescritpion) {
    log.info("Creating new game");
    GameSessionToken gameSessionToken = new GameSessionToken(tokenService.generateNewTokenFor("GAME", allGames));
    GameState gameState = new GameState(gameName, gameDescritpion, gameSessionToken, gameDao, roundResultDao, gameConfigurationDao);
    update(gameState);
    return gameState;
  }

  void update(GameState gameState) {
    log.trace("Updating game: " + gameState);
    allGames.put(gameState.getSessionToken(), gameState);
  }

  // This may be different in the future - Game rather shoudln't be activated right after creation
  public Optional<GameState> activateGame(GameSessionToken token) {
    // TODO: game should be activated only after configuration is done
    GameState _gameState = allGames.get(token);
    if (!_gameState.isGameConfigured()) {
      return Optional.empty();
    }

    _gameState.startGame(token, sessionManager.getUsersFor(token));
    log.debug("Activating game: " + _gameState.getSessionToken());
    update(_gameState);
    return Optional.of(_gameState);
  }

  public boolean isGameActive(GameSessionToken gameSessionToken) {
    log.trace("Checking if game is active: " + gameSessionToken.getStrToken());
    return getGameStateBy(gameSessionToken).map(GameState::isGameActive).orElse(false);
  }

  public boolean isGameConfigured(GameSessionToken gameSessionToken) {
    log.trace("Checking if game is active: " + gameSessionToken.getStrToken());
    return getGameStateBy(gameSessionToken).map(GameState::isGameConfigured).orElse(false);
  }

  public Optional<GameState> getGameStateBy(GameSessionToken gameSessionToken) {
    log.trace("Getting game state for: " + gameSessionToken.getStrToken());
    return Optional.ofNullable(allGames.get(gameSessionToken));
  }

  public GameState setGameConfiguration(GameConfiguration gameConfiguration, GameSessionToken gameSessionToken) {
    log.trace("Setting game configuration {} \t for: {}", gameConfiguration, gameSessionToken.getStrToken());
    allGames.get(gameSessionToken).configureGame(gameConfiguration);
    return allGames.get(gameSessionToken);
  }

  public void deleteGameConfiguration(GameSessionToken gameSessionToken) {
    log.trace("Deleting game configuration for: {}", gameSessionToken.getStrToken());
    allGames.get(gameSessionToken).configureGame(new GameConfiguration());
  }

  public PlayerDecisionOutcomeResponse addPlayerDecision(GameSessionToken gameSessionToken,
      PlayerDecisionRequest playerDecision) {
    return allGames.get(gameSessionToken).addPlayerDecision(playerDecision);
  }

  public boolean hasPlayerAlreadyDecided(GameSessionToken gameSessionToken, PlayerDecisionRequest playerDecision) {
    return allGames.get(gameSessionToken).hasPlayerAlreadyDecided(playerDecision.getUsername());
  }

  public Optional<GameConfiguration> getGameConfigurationFor(GameSessionToken gameSessionToken) {
    log.trace("Getting game configuration for: {}", gameSessionToken.getStrToken());
    return gameExists(gameSessionToken) ? allGames.get(gameSessionToken).getGameConfiguration() : Optional.empty();
  }

  public boolean gameExists(GameSessionToken gameSessionToken) {
    return allGames.containsKey(gameSessionToken);
  }

  public boolean deleteGameConfigurationFor(GameSessionToken gameSessionToken) {
    return gameExists(gameSessionToken) && allGames.get(gameSessionToken).deleteGameConfiguration();
  }

  public HashMap<GameSessionToken, GameState> getAll() {
    return allGames;
  }

  public List<GameState> getAllGameList() {
    return new ArrayList<>(allGames.values());
  }

  public List<GameState> getAllGameListWithName(String name) {
    return allGames.values()
        .stream()
        .filter(gameState -> gameState.getGameName().contains(name))
        .collect(Collectors.toList());
  }

  public List<GameState> getAllGameListWithText(String text) {
    String textLowerCase = text.toLowerCase();

    return allGames.values()
        .stream()
        .filter(gameState -> gameState.getGameName().toLowerCase().contains(textLowerCase)
            || gameState.getDescription().toLowerCase().contains(textLowerCase)
            || gameState.getStatus().toString().toLowerCase().contains(textLowerCase)
            || gameState.getSessionToken().getStrToken().toLowerCase().contains(textLowerCase))
        .collect(Collectors.toList());
  }

  public Optional<GameState> getGameFor(GameSessionToken gameSessionToken) {
    return Optional.ofNullable(allGames.get(gameSessionToken));
  }

  public boolean isGameNameTaken(String gameName) {
    for (GameState gameState : allGames.values()) {
      if (gameName.equals(gameState.getGameName())) {
        return true;
      }
    }
    return false;
  }

  public boolean configure(GameSessionToken gameSessionToken, GameConfiguration gameConfiguration) {
    return !isGameActive(gameSessionToken) && getGameStateBy(gameSessionToken).map(gameState -> {
      gameState.configureGame(gameConfiguration);
      return true;
    }).orElse(false);
  }


  public void updateConnectedUserNum(GameSessionToken gameSessionToken, int updatedConnectedUserNumber){
    allGames.get(gameSessionToken).updateConnectedUserNum(updatedConnectedUserNumber);
  }
}
