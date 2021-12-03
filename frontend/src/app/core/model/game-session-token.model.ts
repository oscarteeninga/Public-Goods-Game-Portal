export class GameSessionToken {
  token: string;

  private constructor(token: string) {
    this.token = token;
  }

  static from(token: string): GameSessionToken {
    return new GameSessionToken(token);
  }

  toString(): string {
    return this.token;
  }
}

export const defaultGameSessionToken: GameSessionToken = GameSessionToken.from('');
