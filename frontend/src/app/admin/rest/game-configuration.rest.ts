import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { GameConfiguration } from '../model/game-configuration.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';


@Injectable({providedIn: 'root'})
export class GameConfigurationRest {

  private readonly baseUrl = `${environment.singApiPrefix}/admin/game`;

  constructor(private http: HttpClient) {
  }

  configureGame(gameSessionToken: GameSessionToken, gameConf: GameConfiguration): Observable<GameConfiguration> {
    return this.http.put<GameConfiguration>(`${this.baseUrl}/configuration/${gameSessionToken}`, gameConf, {responseType: 'json'});
  }

  getGameConfiguration(gameSessionToken: GameSessionToken): Observable<GameConfiguration> {
    return this.http.get<GameConfiguration>(`${this.baseUrl}/configuration/${gameSessionToken}`, {responseType: 'json'});
  }
}
