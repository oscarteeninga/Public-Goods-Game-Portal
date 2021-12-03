export interface GameState {
  gameHistory: GameRoundSummary[];
  walletsState: Map<string, number>;
  playersThatDecided: Set<string>;
}

export const defaultGameState: GameState = {
  gameHistory: [],
  walletsState: new Map(),
  playersThatDecided: new Set(),
};

export interface GameRoundSummary {
  contributions: Map<string, number>;
  payoff: number;
}

export const defaultGameRoundSummary: GameRoundSummary = {
  contributions: new Map(),
  payoff: 0,
};
