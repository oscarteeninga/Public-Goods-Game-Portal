export enum PlayerDecisionOutcome {
  Accepted = 'ACCEPTED',
  RejectedAlreadyDecided = 'REJECTED_ALREADY_DECIDED',
  RejectedBadRound = 'REJECTED_BAD_ROUND',
  RejectedBadValue = 'REJECTED_BAD_VALUE',
  RejectedGameNotActive = 'REJECTED_GAME_NOT_ACTIVE',
  RejectedPlayerNotConnected = 'REJECTED_PLAYER_NOT_CONNECTED',
  RejectedPlayerNotRegistered = 'REJECTED_PLAYER_NOT_REGISTERED',
}
