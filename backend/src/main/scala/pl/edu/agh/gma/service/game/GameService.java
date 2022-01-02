package pl.edu.agh.gma.service.game;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.agh.gma.dto.ConfigurationGameDto;
import pl.edu.agh.gma.dto.CreateNewGameRequest;
import pl.edu.agh.gma.dto.GameInfoResponse;
import pl.edu.agh.gma.dto.gameprogress.GameProgressResponse;
import pl.edu.agh.gma.dto.gameprogress.GameStateResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionOutcomeResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionRequest;
import pl.edu.agh.gma.error.exceptions.GameCreationException;
import pl.edu.agh.gma.error.exceptions.GameNotExistsException;
import pl.edu.agh.gma.mapper.GameConfigurationMapper;
import pl.edu.agh.gma.mapper.GameInfoMapper;
import pl.edu.agh.gma.mapper.GameStateMapper;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.mongo.GameConfiguration;
import pl.edu.agh.gma.service.user.UserManager;
import pl.edu.agh.gma.service.util.GameState;
import pl.edu.agh.gma.service.util.SessionManager;

@Slf4j
@Service
@AllArgsConstructor
public class GameService {

  private final UserManager userManager;
  private final GamesStateManager gamesStateManager;
  private final SessionManager sessionManager;

  private final GameConfigurationDao gameConfigurationDao;

  private final GameConfigurationMapper gameConfigurationMapper;
  private final GameInfoMapper gameInfoMapper;
  private final GameStateMapper gameStateMapper;

  // public RoundResults getLastRoundResult() {
  //   return gameHistory.getLastRoundResult();
  // }

  // public List<RoundResults> getAllHistory() {
  //   return gameHistory.getWholeGameHistory();
  // }

  public PlayerDecisionOutcomeResponse addPlayerDecision(GameSessionToken gameSessionToken,
      PlayerDecisionRequest playerDecisionRequest) {
    if (!userManager.isUserRegisteredByName(playerDecisionRequest.getUsername())) {
      log.warn("Player $(" + playerDecisionRequest.getUsername() + ") is not registered.");
      return PlayerDecisionOutcomeResponse.REJECTED_PLAYER_NOT_REGISTERED;
    }

    if (!gamesStateManager.isGameActive(gameSessionToken)) {
      log.warn("Game $(" + gameSessionToken + ") is not active.");
      return PlayerDecisionOutcomeResponse.REJECTED_GAME_NOT_ACTIVE;
    }

    if (!sessionManager.isUserConnectedToGame(playerDecisionRequest.getUsername(), gameSessionToken)) {
      log.warn("Player $(" + playerDecisionRequest.getUsername() + ") is not connected to Game $(" + gameSessionToken
          + ").");
      return PlayerDecisionOutcomeResponse.REJECTED_PLAYER_NOT_CONNECTED;
    }

    return gamesStateManager.addPlayerDecision(gameSessionToken, playerDecisionRequest);
  }

  public Optional<ConfigurationGameDto> getGameConfiguration(GameSessionToken gameSessionToken) {
    ConfigurationGameDto configurationGameDto = gamesStateManager.getGameStateBy(gameSessionToken)
        .map(gameState -> gameConfigurationMapper.mapToDto(gameState, gameState.getGameConfiguration()))
        .orElseThrow(() -> new GameNotExistsException(gameSessionToken.getStrToken()));
    return Optional.ofNullable(configurationGameDto);
  }

  public Optional<ConfigurationGameDto> configure(ConfigurationGameDto gameConfigurationDto) {
    log.info("Configure Game: {}", gameConfigurationDto);
    GameSessionToken gameSessionToken = gameConfigurationMapper.mapToGameSessionToken(gameConfigurationDto);
    checkGameSessionToken(gameSessionToken);  // throwable

    final GameConfiguration gameConfiguration = gameConfigurationMapper.mapToGameConf(gameConfigurationDto);

    return (gamesStateManager.configure(gameSessionToken, gameConfiguration)) ?
        Optional.ofNullable(gameConfigurationMapper.mapToDto(
            gamesStateManager.getGameStateBy(gameSessionToken).get(),
            gamesStateManager.getGameConfigurationFor(gameSessionToken)
        )) : Optional.empty();
  }

  private void checkGameSessionToken(GameSessionToken gameSessionToken) {
    if (!gamesStateManager.gameExists(gameSessionToken)) {
      throw new GameNotExistsException(gameSessionToken.getStrToken());
    }
  }

  public boolean deleteGameConfigurationFor(GameSessionToken gameSessionToken) {
    if (gamesStateManager.gameExists(gameSessionToken)) {
      Optional<GameConfiguration> optGameConfiguration = gamesStateManager.getGameConfigurationFor(gameSessionToken);
      if (optGameConfiguration.isPresent()) {
        gameConfigurationDao.delete(optGameConfiguration.get());
        gamesStateManager.deleteGameConfiguration(gameSessionToken);
        return true;
      }
    }
    return false;
  }

  public List<GameInfoResponse> getAllGames() {
    List<GameState> gameStates = gamesStateManager.getAllGameList();
    return gameStates.stream().map(gameInfoMapper::mapToGameInfoResponse).collect(Collectors.toList());
  }

  public List<GameInfoResponse> getAllGamesWithName(String name) {
    List<GameState> gameStates = gamesStateManager.getAllGameListWithName(name);
    return gameStates.stream().map(gameInfoMapper::mapToGameInfoResponse).collect(Collectors.toList());
  }

  public List<GameInfoResponse> getAllGamesWithText(String text) {
    List<GameState> gameStates = gamesStateManager.getAllGameListWithText(text);
    return gameStates.stream().map(gameInfoMapper::mapToGameInfoResponse).collect(Collectors.toList());
  }

  public Optional<GameInfoResponse> getGame(GameSessionToken gameSessionToken) {
    Optional<GameState> gameState = gamesStateManager.getGameFor(gameSessionToken);

    return gameState.map(gameInfoMapper::mapToGameInfoResponse);
  }

  public GameInfoResponse updateGame(ConfigurationGameDto configurationGameDto, GameSessionToken gameSessionToken) {
    checkGameSessionToken(gameSessionToken); // throwable

    GameConfiguration gameConfiguration = gameConfigurationMapper.mapToGameConf(configurationGameDto);

    GameState gameState =
        gamesStateManager.setGameConfiguration(gameConfigurationDao.insert(gameConfiguration), gameSessionToken);

    return gameInfoMapper.mapToGameInfoResponse(gameState);
  }

  public GameInfoResponse createNewGame(CreateNewGameRequest newGameRequest) {
    String newGameName = newGameRequest.getGameName().trim();
    if (gamesStateManager.isGameNameTaken(newGameName)) {
      throw new GameCreationException(newGameName);
    }

    GameState gameState = gamesStateManager.createGame(newGameName, newGameRequest.getDescription());

    return gameInfoMapper.mapToGameInfoResponse(gameState);
  }

  public Optional<GameInfoResponse> activateAndStartGame(GameSessionToken gameSessionToken) {

    return gamesStateManager.activateGame(gameSessionToken)
        .map(gameInfoMapper::mapToGameInfoResponse);
  }

  public GameProgressResponse getGameProgressResponse(GameSessionToken gameSessionToken){
    Optional<GameState> optGameState = gamesStateManager.getGameStateBy(gameSessionToken);
    if (optGameState.isPresent() && optGameState.get().isGameActive())
      optGameState.get().invokeUpdateGame();
    return optGameState.map(gameStateMapper::mapToGameProgressResponse)
              .orElse(gameStateMapper.getCleanGameProgressResponse());
  }

  public GameStateResponse getGameStateResponse(GameSessionToken gameSessionToken){
    Optional<GameState> optGameState = gamesStateManager.getGameStateBy(gameSessionToken);
    return optGameState.map(gameStateMapper::mapToGameStateResponse)
            .orElse(gameStateMapper.getCleanGameStateResponse());
  }
}