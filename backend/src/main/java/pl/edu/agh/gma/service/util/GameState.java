package pl.edu.agh.gma.service.util;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.gma.dto.gameprogress.GameStateResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionOutcomeResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionRequest;
import pl.edu.agh.gma.error.exceptions.GameConfigurationException;
import pl.edu.agh.gma.error.exceptions.GameIsAlreadyFinishedException;
import pl.edu.agh.gma.mapper.GameStateMapper;
import pl.edu.agh.gma.model.artificial.ArtificialUser;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.GameStatus;
import pl.edu.agh.gma.model.artificial.Casual;
import pl.edu.agh.gma.model.artificial.Cooperator;
import pl.edu.agh.gma.model.artificial.FreeRider;
import pl.edu.agh.gma.model.mongo.GameConfiguration;
import pl.edu.agh.gma.model.mongo.GameDetails;
import pl.edu.agh.gma.model.mongo.PlayerDecisionSummary;
import pl.edu.agh.gma.model.mongo.RoundResults;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.RoundResultDao;
import pl.edu.agh.gma.service.cache.RoundMemory;
import pl.edu.agh.gma.service.cache.Wallets;
import pl.edu.agh.gma.service.game.GameConfigurationDao;
import pl.edu.agh.gma.service.game.GameDao;
import pl.edu.agh.gma.service.game.GameEngine;

/**
 * Stores!!! information about concrete single game
 * <p>
 * <p>
 * 1. Game is in CREATED state
 * <p>
 * 2. Game is configured (CREATED -> CONFIGURED) state
 * <p>
 * 3. Game is started (CONFIGURED -> ACTIVE):
 * 3.1 Cannot add any more players (player list is frozen)
 * 3.2 First round with initial time is created
 * <p>
 * 4. If game is ACTIVE players can send their request with decision:
 * 4.1 Each request should be processed only once
 * 4.2 Each request should be send within round time
 * 4.3 Each request bumps actual time (if timer is out of round range)
 * 4.3.1 current round is stored in DB
 * 4.3.2 new round is created
 */
@Slf4j
public class GameState {

  /* Information about this game with configuration etc. */
  private final GameDetails currentGameDetails;

  private final GameEngine gameEngine;

  private final Wallets wallets;

  /* Locks for concurrency */
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

  /* Information about current round and other information. */
  private RoundMemory currentRoundMemory;
  private long gameStartTimestamp = 0;

  private final GameStateMapper gameStateMapper;
  private final GameDao gameDao;
  private final RoundResultDao roundResultDao;
  private final GameConfigurationDao gameConfigurationDao;

  private List<ArtificialUser> artificialUsers = new ArrayList<>();

  public GameState(String gameName, String gameDescription, GameSessionToken sessionToken,
                   GameDao gameDao, RoundResultDao roundResultDao, GameConfigurationDao gameConfigurationDao) {
    this.currentGameDetails = new GameDetails(gameName, gameDescription, GameStatus.CREATED, sessionToken);
    this.currentRoundMemory = new RoundMemory();
    this.gameEngine = new GameEngine();
    this.wallets = new Wallets();
    this.gameStateMapper = new GameStateMapper();
    this.gameDao = gameDao;
    this.roundResultDao = roundResultDao;
    this.gameConfigurationDao = gameConfigurationDao;
  }

  public List<ArtificialUser> artificialUsers() {
    int casuals = this.getGameConfiguration().map(GameConfiguration::getCasuals).orElse(0);
    int cooperators = this.getGameConfiguration().map(GameConfiguration::getCooperators).orElse(0);
    int freeriders = this.getGameConfiguration().map(GameConfiguration::getFreeriders).orElse(0);

    List<ArtificialUser> artificialUsers = new ArrayList<>();
    IntStream.range(0, casuals).forEach(id -> artificialUsers.add(new Casual(id)));
    IntStream.range(0, cooperators).forEach(id -> artificialUsers.add(new Cooperator(id)));
    IntStream.range(0, freeriders).forEach(id -> artificialUsers.add(new FreeRider(id)));

    return artificialUsers;
  }

  public void startGame(GameSessionToken sessionToken, List<User> connectedUsers) {
    writeLock.lock();
    try {
      assert (sessionToken.equals(this.currentGameDetails.getSessionToken()));
      ensureGameNotFinished();
      this.artificialUsers = artificialUsers();
      connectedUsers.addAll(this.artificialUsers);
      this.currentGameDetails.setRoundsNum(1);
      this.currentGameDetails.setConnectedUsers(connectedUsers);
      this.currentGameDetails.setConnectedUserNum(connectedUsers.size());
      gameStartTimestamp = System.currentTimeMillis();
      this.currentGameDetails.setStatus(GameStatus.ACTIVE);

      List<PlayerDecisionSummary> playerDecisionsBeforeFirstRound = connectedUsers.stream()
          .map(user -> new PlayerDecisionSummary(sessionToken, user.getUsername(), 0,
              currentGameDetails.getGameConfiguration().getInitialMoneyAmount(), 0.0, 0.0,
              currentGameDetails.getGameConfiguration().getInitialMoneyAmount()))
          .collect(Collectors.toList());

      wallets.initializeWalletStates(currentGameDetails.getGameConfiguration().getInitialMoneyAmount(), connectedUsers);

      log.info("Starting game: " + sessionToken + " with " + connectedUsers.size() + " users.");
      log.debug("Initialization of connected users: " + Arrays.toString(connectedUsers.toArray()));
      log.trace(Arrays.toString(playerDecisionsBeforeFirstRound.toArray()));

      storeRoundResults(new RoundResults(0, playerDecisionsBeforeFirstRound));

      System.out.println(getConnectedUserNum());


      if (getConnectedUserNum() == this.artificialUsers.size()) {
        this.pullUpGameStatus();
      }
    } finally {
      writeLock.unlock();
    }
  }

  private void ensureGameNotFinished() {
    if (currentGameDetails.getStatus().equals(GameStatus.FINISHED)) {
      throw new GameIsAlreadyFinishedException(currentGameDetails.getSessionToken().getStrToken());
    }
  }

  private void finishGame() {
    log.debug("FINISHING game! " + currentGameDetails);
    ensureGameNotFinished();
    this.currentGameDetails.setStatus(GameStatus.FINISHED);
  }

  public PlayerDecisionOutcomeResponse addPlayerDecision(PlayerDecisionRequest playerDecision) {
    writeLock.lock();
    try {
      ensureGameNotFinished();
      if (isContributionValid(playerDecision)) {
        log.warn("Player $(" + playerDecision.getUsername() + ") tries to contribute wrong amount: " + playerDecision.getContribution());
        return PlayerDecisionOutcomeResponse.REJECTED_BAD_VALUE;
      }
      return currentRoundMemory.addPlayerDecision(playerDecision);
    } finally {
      writeLock.unlock();
    }
  }

  private boolean isContributionValid(PlayerDecisionRequest pDR) {
    return pDR.getContribution() <= 0 || wallets.getCurrentMoney(pDR.getUsername()) < pDR.getContribution();
  }

  public void configureGame(GameConfiguration gameConfiguration) {
    writeLock.lock();
    try {
      ensureGameCanBeConfigured();
      GameConfiguration _gameConfiguration = gameConfigurationDao.insert(gameConfiguration);
      currentGameDetails.setGameConfiguration(_gameConfiguration);
      currentGameDetails.setStatus(GameStatus.CONFIGURED);
      storeGameDetails();
    } finally {
      writeLock.unlock();
    }
  }

  private void ensureGameCanBeConfigured() {
    if (currentGameDetails.getStatus().equals(GameStatus.ACTIVE) || currentGameDetails.getStatus()
        .equals(GameStatus.FINISHED)) {
      throw new GameConfigurationException(currentGameDetails.getSessionToken().getStrToken());
    }
  }

  public RoundMemory getCurrentRoundMemory() {
    return currentRoundMemory;
  }

  public boolean deleteGameConfiguration() {
    currentGameDetails.setGameConfiguration(null);
    return true;
  }

  public boolean hasPlayerAlreadyDecided(String username) {
    return currentRoundMemory.hasPlayerAlreadyDecided(username);
  }

  public int getCurrentRound() {
    return currentRoundMemory.getCurrentRoundNum();
  }

  public Optional<GameStateResponse> invokeUpdateGame() {
    writeLock.lock();
    try {
      return pullUpGameStatus();
    } finally {
      writeLock.unlock();
    }
  }

  private void updateTime() {
    gameStartTimestamp = System.currentTimeMillis() - 1000L * currentRoundMemory.getCurrentRoundNum() * currentGameDetails.getGameConfiguration().getRoundTime();
  }

  private void artificialDecisions() {
    artificialUsers.forEach(user -> {
      currentRoundMemory.addPlayerDecision(
          new PlayerDecisionRequest(
              user.getUsername(),
              currentRoundMemory.getCurrentRoundNum(),
              user.contribution(wallets.getCurrentMoney(user.getUsername()))
          )
      );
    });
  }

  // dociagniecie current round num
  private Optional<GameStateResponse> pullUpGameStatus() {
    log.debug("UPDATE Curremt game time: " + getCurrentGameTime() + " / " +
        1000L * currentRoundMemory.getCurrentRoundNum() * currentGameDetails.getGameConfiguration().getRoundTime()
    );

    logAggState();

    if (getCurrentGameTime() > 1000L * currentRoundMemory.getCurrentRoundNum() * currentGameDetails.getGameConfiguration().getRoundTime() ||
        currentGameDetails.getConnectedUserNum() == currentRoundMemory.getPlayersThatDecided().size() + artificialUsers.size()) {
      this.artificialDecisions();
      this.updateTime();
      log.trace("Going to next round: " + (currentRoundMemory.getCurrentRoundNum() <= currentGameDetails.getGameConfiguration().getNumberOfRounds())
          + "; cuurent=" + currentRoundMemory.getCurrentRoundNum() + "; total=" + currentGameDetails.getGameConfiguration().getNumberOfRounds()
      );
      if (currentRoundMemory.getCurrentRoundNum() <= currentGameDetails.getGameConfiguration().getNumberOfRounds()) {

        List<PlayerDecisionSummary> playerRoundSummaries = goToNextRound();

        storeRoundResults(new RoundResults(currentRoundMemory.getCurrentRoundNum(), playerRoundSummaries));
      }

      if (currentRoundMemory.getCurrentRoundNum() > currentGameDetails.getGameConfiguration().getNumberOfRounds())
        finishGame();
      else if (this.getConnectedUserNum() == this.artificialUsers.size()) {
        pullUpGameStatus();
      }

      return Optional.ofNullable(gameStateMapper.mapToGameStateResponse(this));
    }
    return Optional.empty();
  }

  private void logAggState() {
    log.info("Round: " + this.currentRoundMemory.getCurrentRoundNum()
        + " #players: " + this.currentRoundMemory.getPlayersThatDecided().size()
        + "/" + this.currentGameDetails.getConnectedUserNum()
        + " playersDecided: " + this.currentRoundMemory.getPlayersThatDecided());

    log.info("Wallets: " + wallets);

    log.info("List<RoundResults>: " + currentGameDetails.getRoundList());
  }


  private void storeRoundResults(RoundResults roundResults) {
    RoundResults r = roundResultDao.save(roundResults);
    log.debug("Storing round result: " + r.toString());
    currentGameDetails.add(r);

    log.trace(roundResults.toString());
    storeGameDetails();
  }

  private void storeGameDetails() {
    log.debug("Storing game details for " + currentGameDetails.getSessionToken());
    log.trace(currentGameDetails.toString());
    gameDao.save(currentGameDetails);
  }

  private List<PlayerDecisionSummary> goToNextRound() {

    double roundPayOff = gameEngine.calculateRoundPayoff(currentRoundMemory,
        currentGameDetails.getGameConfiguration().getPoolMultiplierFactor());

    log.trace("Before wallets state: " + wallets);

    List<PlayerDecisionSummary> playerDecisionSummaryList = currentRoundMemory.getPlayerDecisionList()
        .stream()
        .filter(pdRequest -> wallets.haveEnoughMoney(pdRequest.getUsername(), pdRequest.getContribution()))
        .map(pdRequest -> {
          double moneyBeforeTransaction = wallets.getCurrentMoney(pdRequest.getUsername());
          wallets.takeMoneyFrom(pdRequest.getUsername(), pdRequest.getContribution());
          wallets.giveMoneyTo(pdRequest.getUsername(), roundPayOff);
          return new PlayerDecisionSummary(
              currentGameDetails.getSessionToken(),
              pdRequest.getUsername(),
              pdRequest.getRoundNum(),
              moneyBeforeTransaction,
              pdRequest.getContribution(),
              roundPayOff,
              wallets.getCurrentMoney(pdRequest.getUsername())
          );
        })
        .collect(Collectors.toList());

    log.trace("");
    log.trace("Round Summary: roundPayOff: " + roundPayOff + ", playerRoundSummaryList: " + Arrays.toString(
        playerDecisionSummaryList.toArray()) + ", wallets " + wallets);
    log.debug("Going to next round from: " + currentRoundMemory.getCurrentRoundNum());
    log.debug("Game Details: " + currentGameDetails);
    log.trace("");

    currentRoundMemory = currentRoundMemory.nextRoundMemory();
    currentGameDetails.setRoundsNum(currentRoundMemory.getCurrentRoundNum());
    return playerDecisionSummaryList;
  }

  private long getCurrentGameTime() {
    return System.currentTimeMillis() - gameStartTimestamp;
  }

  public boolean isGameActive() {
    readLock.lock();
    try {
      return this.currentGameDetails.getStatus().equals(GameStatus.ACTIVE);
    } finally {
      readLock.unlock();
    }
  }

  private boolean isGameActiveUnlocked() {
    return this.currentGameDetails.getStatus().equals(GameStatus.ACTIVE);
  }

  public boolean isGameConfigured() {
    readLock.lock();
    try {
      return this.currentGameDetails.getStatus().equals(GameStatus.CONFIGURED);
    } finally {
      readLock.unlock();
    }
  }

  public long getGameTimeLeft() {
    return Math.max(0,
        (long) currentGameDetails.getGameConfiguration().getRoundTime() * currentGameDetails.getGameConfiguration().getNumberOfRounds()
            - getCurrentGameTime() / 1000
    );
  }

  public long getRoundTimeLeft() {
    return Math.max(0,
        1000L * currentGameDetails.getGameConfiguration().getRoundTime() * currentRoundMemory.getCurrentRoundNum()
            - getCurrentGameTime()
    );
  }

  public GameSessionToken getSessionToken() {
    readLock.lock();
    try {
      return this.currentGameDetails.getSessionToken();
    } finally {
      readLock.unlock();
    }
  }

  // ======================================== Game getters ========================================

  public String getGameName() {
    readLock.lock();
    try {
      return this.currentGameDetails.getGameName();
    } finally {
      readLock.unlock();
    }
  }

  public String getDescription() {
    readLock.lock();
    try {
      return this.currentGameDetails.getDescription();
    } finally {
      readLock.unlock();
    }
  }

  public GameStatus getStatus() {
    readLock.lock();
    try {
      return this.currentGameDetails.getStatus();
    } finally {
      readLock.unlock();
    }
  }

  public String getGameID() {
    readLock.lock();
    try {
      return this.currentGameDetails.getId();
    } finally {
      readLock.unlock();
    }
  }

  public Optional<GameConfiguration> getGameConfiguration() {
    readLock.lock();
    try {
      return Optional.ofNullable(currentGameDetails.getGameConfiguration());
    } finally {
      readLock.unlock();
    }
  }

  public int getConnectedUserNum() {
    readLock.lock();
    try {
      return currentGameDetails.getConnectedUserNum();
    } finally {
      readLock.unlock();
    }
  }

  public List<User> getConnectedUsers() {
    readLock.lock();
    try {
      return currentGameDetails.getConnectedUsers();
    } finally {
      readLock.unlock();
    }
  }

  public int getRoundsNum() {
    readLock.lock();
    try {
      return currentGameDetails.getRoundsNum();
    } finally {
      readLock.unlock();
    }
  }

  public List<RoundResults> getRoundList() {
    readLock.lock();
    try {
      return currentGameDetails.getRoundList();
    } finally {
      readLock.unlock();
    }
  }

  private GameDetails getCurrentGameDetails() {
    return currentGameDetails;
  }

  public List<RoundResults> getRoundListResults() {
    return this.currentGameDetails.getRoundList();
  }

  public Map<String, Double> getWallets() {
    return this.wallets.getAllUserWallets();
  }

  public void updateConnectedUserNum(int updatedConnectedUserNumber) {
    log.debug("New connected users: " + updatedConnectedUserNumber + " for game: " + currentGameDetails.getSessionToken());
    writeLock.lock();
    try {
      this.currentGameDetails.setConnectedUserNum(updatedConnectedUserNumber);
      storeGameDetails();
    } finally {
      writeLock.unlock();
    }
  }
}
