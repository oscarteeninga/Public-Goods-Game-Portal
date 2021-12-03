import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GameProgress } from '../model/game-progress.model';
import { GameState } from '../model/game-state.model';
import { GameProgressRest } from '../rest/game-progress.rest';
import { GameSessionToken } from '../../core/model/game-session-token.model';
import { PlayerDecisionOutcome } from '../model/player-decision-outcome.model';
import { PlayerDecision } from '../model/player-decision.model';

@Injectable({
  providedIn: 'root'
})
export class GameProgressService {

  constructor(private gameProgressRest: GameProgressRest) {
  }

  getProgress(gameSessionToken: GameSessionToken): Observable<GameProgress> {
    return this.gameProgressRest.getProgress(gameSessionToken);
  }

  getState(gameSessionToken: GameSessionToken): Observable<GameState> {
    return this.gameProgressRest.getState(gameSessionToken);
  }

  sendDecision(gameSessionToken: GameSessionToken, playerDecision: PlayerDecision): Observable<PlayerDecisionOutcome> {
    return this.gameProgressRest.sendDecision(gameSessionToken, playerDecision);
  }
}
