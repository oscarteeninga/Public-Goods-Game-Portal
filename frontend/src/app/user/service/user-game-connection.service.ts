import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserGameConnectionRest } from '../rest/user-game-connection.rest';
import { SessionInfo } from '../model/session-info.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';

@Injectable({
  providedIn: 'root'
})
export class UserGameConnectionService {

  constructor(private userGameConnectionRest: UserGameConnectionRest) {
  }

  connect2(sessionInfo: SessionInfo): Observable<any> {
    return this.userGameConnectionRest.connect2(sessionInfo);
  }

  connect(username: string, gameSessionToken: GameSessionToken): Observable<boolean> {
    return this.userGameConnectionRest.connect(username, gameSessionToken);
  }

  isConnected(username: string, gameSessionToken: GameSessionToken): Observable<boolean> {
    return this.userGameConnectionRest.isConnected(username, gameSessionToken);
  }
}
