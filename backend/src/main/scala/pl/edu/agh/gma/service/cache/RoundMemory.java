package pl.edu.agh.gma.service.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionOutcomeResponse;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionRequest;

@Slf4j
public class RoundMemory {

  private final int currentRoundNum;

  // key = user.name, value = PlayerDecision
  private final HashMap<String, PlayerDecisionRequest> playerDecisionHashMap;

  /**
   * constructors
   */
  public RoundMemory() {
    this(1);
  }

  private RoundMemory(int roundNum) {
    this.currentRoundNum = roundNum;
    this.playerDecisionHashMap = new HashMap<>();
  }

  public RoundMemory nextRoundMemory() {
    return new RoundMemory(this.currentRoundNum + 1);
  }

  /* Adders */

  /**
   * Adds playerDecision and cache it in memory
   *
   * @param playerDecision single user decision in a round
   *
   * @return ACCEPT or REJECT_[reason] (see PlayerDecisionOutcomeResponse enum)
   */
  public PlayerDecisionOutcomeResponse addPlayerDecision(PlayerDecisionRequest playerDecision) {

    /* keeps track if user already voted */
    if (hasPlayerAlreadyDecided(playerDecision)) {
      //TODO change warn to debug
      log.warn("Player already decided: " + playerDecision);
      //TODO #9 this is checked twice - here and in GameService
      return PlayerDecisionOutcomeResponse.REJECTED_ALREADY_DECIDED;
    }
    /* asserts that player decision is equal to current round */
    if (isPlayerDecisionForOtherRound(playerDecision)) {
      //TODO change warn to debug
      log.warn("Player decision is not for this round! " + "currentRoundNum=" + currentRoundNum + ",   playerDecision="
          + playerDecision);
      return PlayerDecisionOutcomeResponse.REJECTED_BAD_ROUND;
    }

    log.info("Player " + playerDecision.getUsername() + " decision accepted! currentRoundNum=" + currentRoundNum
      + ", #decisions=" + playerDecisionHashMap.size());
    playerDecisionHashMap.put(playerDecision.getUsername(), playerDecision);

    return PlayerDecisionOutcomeResponse.ACCEPTED;
  }

  /**
   * Validators
   */
  private boolean isPlayerDecisionForOtherRound(PlayerDecisionRequest playerDecision) {
    return playerDecision.getRoundNum() != currentRoundNum;
  }

  private boolean hasPlayerAlreadyDecided(PlayerDecisionRequest playerDecision) {
    return hasPlayerAlreadyDecided(playerDecision.getUsername());
  }

  public boolean hasPlayerAlreadyDecided(String username) {
    return playerDecisionHashMap.containsKey(username);
  }

  /**
   * Getters
   */
  public int getCurrentRoundNum() {
    return currentRoundNum;
  }

  public Map<String, PlayerDecisionRequest> getPlayerDecisionsHashMap() {
    return playerDecisionHashMap;
  }

  public List<PlayerDecisionRequest> getPlayerDecisionList() {
    return new ArrayList<>(playerDecisionHashMap.values());
  }

  public Set<String> getPlayersThatDecided() {
    return playerDecisionHashMap.keySet();
  }

}
