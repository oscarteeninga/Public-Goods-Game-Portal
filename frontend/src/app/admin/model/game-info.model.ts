import { defaultGameSessionToken, GameSessionToken } from '../../core/model/game-session-token.model';
import { GameStatus } from '../../core/model/game-status';

export interface GameInfo {
  id?: string;
  gameSessionToken: GameSessionToken;

  gameName: string;
  description: string;

  status: GameStatus;
  connectedPlayers?: number;

  linkForPlayers?: string;
}

export const defaultGameInfo: GameInfo = {
  gameSessionToken: defaultGameSessionToken,
  gameName: '',
  description: '',

  status: GameStatus.Created,
  connectedPlayers: 0,
  linkForPlayers: ''
};


export interface InitGameInfo {
  gameName: string;
  description: string;
}
