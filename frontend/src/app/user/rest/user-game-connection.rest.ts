import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { SessionInfo } from '../model/session-info.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';


@Injectable({providedIn: 'root'})
export class UserGameConnectionRest {

  private readonly baseUrl = `${environment.singApiPrefix}/game/session`;

  constructor(private http: HttpClient) {
  }

  connect2(sessionInfo: SessionInfo): Observable<any> {
    return this.http.post(`${this.baseUrl}/connect`, sessionInfo, {responseType: 'json'});
  }

  connect(username: string, gameSessionToken: GameSessionToken): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/connect?gameSession=${gameSessionToken}&username=${username}`, {responseType: 'json'});
  }

  isConnected(username: string, gameSessionToken: GameSessionToken): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/isconnected?gameSession=${gameSessionToken}&username=${username}`, {responseType: 'json'});
  }
}
