import { GameStatus } from '../../core/model/game-status';

export interface GameProgress {
  id?: string;

  status: GameStatus;
  round: number;
  timeLeftMs: number;
}

export const defaultGameProgress: GameProgress = {
  status: GameStatus.Created,
  round: 1,
  timeLeftMs: 0,
};
