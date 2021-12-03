package pl.edu.agh.gma.dto.gameprogress;

public enum PlayerDecisionOutcomeResponse {
  ACCEPTED,                       // decision accepted
  REJECTED_ALREADY_DECIDED,       // decision rejected - player already submitted decision for this round
  REJECTED_BAD_ROUND,             // decision rejected - current round is different from the one in request
  REJECTED_BAD_VALUE,             // decision rejected - value negative or greater than currently possessed by player
  REJECTED_GAME_NOT_ACTIVE,       // decision rejected - game is not active and does not accept decisions
  REJECTED_PLAYER_NOT_CONNECTED,  // decision rejected - player is not connected to this game
  REJECTED_PLAYER_NOT_REGISTERED, // decision rejected - player is not registered
}
