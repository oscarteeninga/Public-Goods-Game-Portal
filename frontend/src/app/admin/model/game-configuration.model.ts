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
}


export const defaultGameConfiguration: GameConfiguration = {
  gameSessionToken: defaultGameSessionToken,
  gameName: '',
  description: '',
  roundTime: -1,
  numberOfRounds: -1,
  initialMoneyAmount: -1,
  poolMultiplierFactor: -1,
  configured: false,
  gameStatus: '',
  gameLink: '',

};
