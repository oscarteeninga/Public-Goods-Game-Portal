import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GameInfo, InitGameInfo } from '../model/game-info.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';
import { GameManagementRest } from "../rest/game-management.rest";

@Injectable({
  providedIn: 'root'
})
export class GameManagementService {

  constructor(private gameManagementRest: GameManagementRest) {
  }

  createGame(data: InitGameInfo): Observable<GameInfo> {
    return this.gameManagementRest.createGame(data);
  }

  deleteGame(gameSessionToken: GameSessionToken): Observable<any> {
    return this.gameManagementRest.deleteGame(gameSessionToken);
  }

  activateAndStartGame(gameSessionToken: GameSessionToken): Observable<GameInfo> {
    return this.gameManagementRest.activateAndStartGame(gameSessionToken);
  }
}
