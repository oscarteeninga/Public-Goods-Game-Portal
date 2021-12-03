import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { GameProgress } from '../model/game-progress.model';
import { GameState } from '../model/game-state.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';
import { PlayerDecision } from '../model/player-decision.model';
import { PlayerDecisionOutcome } from '../model/player-decision-outcome.model';


@Injectable({providedIn: 'root'})
export class GameProgressRest {

  private readonly baseUrl = `${environment.singApiPrefix}/play/game`;

  constructor(private http: HttpClient) {
  }


  getProgress(gameSessionToken: GameSessionToken): Observable<GameProgress> {
    return this.http.get<GameProgress>(`${this.baseUrl}/progress/${gameSessionToken}`, {responseType: 'json'});
  }

  getState(gameSessionToken: GameSessionToken): Observable<GameState> {
    return this.http.get<GameState>(`${this.baseUrl}/state/${gameSessionToken}`, {responseType: 'json'});
  }

  sendDecision(gameSessionToken: GameSessionToken, playerDecision: PlayerDecision): Observable<PlayerDecisionOutcome> {
    return this.http.put<PlayerDecisionOutcome>(`${this.baseUrl}/decision/${gameSessionToken}`, playerDecision, {responseType: 'json'});
  }
}
