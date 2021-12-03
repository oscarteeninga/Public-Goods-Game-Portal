import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { GameInfo, InitGameInfo } from '../model/game-info.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';


@Injectable({providedIn: 'root'})
export class GameManagementRest {

  private readonly baseUrl = `${environment.singApiPrefix}/admin/game`;

  constructor(private http: HttpClient) {
  }

  createGame(data: InitGameInfo): Observable<GameInfo> {
    return this.http.post<GameInfo>(`${this.baseUrl}/new`, data, {responseType: 'json'});
  }

  deleteGame(gameSessionToken: GameSessionToken): Observable<any> {
    return this.http.delete(`${this.baseUrl}/configuration/${gameSessionToken}`, {responseType: 'json'});
  }


  activateAndStartGame(gameSessionToken: GameSessionToken): Observable<GameInfo> {
    return this.http.get<GameInfo>(`${this.baseUrl}/activate/${gameSessionToken}`, {responseType: 'json'});
  }
}
