export interface PlayerDecision {
  username: string;
  roundNum: number;
  contribution: number;
}

export const defaultPlayerDecision: PlayerDecision = {
  username: '',
  roundNum: 0,
  contribution: 0,
};
