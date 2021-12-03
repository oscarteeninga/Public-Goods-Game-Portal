import { defaultGameSessionToken, GameSessionToken } from '../../core/model/game-session-token.model';

export interface GameConfiguration {

  gameSessionToken: GameSessionToken;

  gameName: string;
  description: string;

  roundTime: number;
  numberOfRounds: number;
  initialMoneyAmount: number;
  poolMultiplierFactor: number;

  configured: boolean;
  gameStatus: string;
  gameLink: string;

  freeriders: 0,
  cooperators: 0,
  casuals: 0,
}

export interface ArtificialPlayers {
  freeriders: number;
  cooperators: number;
  casuals: number;
}

export const defaultGameConfiguration: GameConfiguration = {
  gameSessionToken: defaultGameSessionToken,
  gameName: '',
  description: '',
  roundTime: 1,
  numberOfRounds: 1,
  initialMoneyAmount: 10,
  poolMultiplierFactor: 2,
  configured: false,
  gameStatus: '',
  gameLink: '',
  freeriders: 0,
  cooperators: 0,
  casuals: 0,
};




