import { Injectable } from '@angular/core';
import { GameInformationRest } from '../rest/game-information.rest';
import { Observable } from 'rxjs';
import { GameInfo } from '../model/game-info.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';

@Injectable({
  providedIn: 'root'
})
export class GameFindingService {

  constructor(private gameInformationRest: GameInformationRest) {
  }

  /** Getters */
  getGame(gameSessionToken: GameSessionToken): Observable<GameInfo> {
    return this.gameInformationRest.getGame(gameSessionToken);
  }

  getAllGames(): Observable<GameInfo[]> {
    return this.gameInformationRest.getAllGames();
  }

  findGamesByName(gameNamePart: string): Observable<GameInfo[]> {
    return this.gameInformationRest.getAllGamesByName(gameNamePart);
  }

  findGamesContainingText(text: string): Observable<GameInfo[]> {
    return this.gameInformationRest.getAllGamesWithText(text);

  }

  /** ================= */

}
