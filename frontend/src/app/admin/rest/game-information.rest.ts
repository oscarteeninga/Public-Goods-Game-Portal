import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { GameInfo } from '../model/game-info.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';


@Injectable({providedIn: 'root'})
export class GameInformationRest {

  private readonly baseUrl = `${environment.singApiPrefix}/admin/games/find`;

  constructor(private http: HttpClient) {
  }


  getAllGames(): Observable<GameInfo[]> {
    return this.http.get<GameInfo[]>(`${this.baseUrl}/all`, {responseType: 'json'});
  }

  getAllGamesByName(gameNamePart: string): Observable<GameInfo[]> {
    return this.http.get<GameInfo[]>(`${this.baseUrl}/name/${gameNamePart}`, {responseType: 'json'});
  }

  getAllGamesWithText(text: string): Observable<GameInfo[]> {
    return this.http.get<GameInfo[]>(`${this.baseUrl}/text/${text}`, {responseType: 'json'});
  }

  getGame(gameSessionToken: GameSessionToken): Observable<GameInfo> {
    return this.http.get<GameInfo>(`${this.baseUrl}/${gameSessionToken}`, {responseType: 'json'});
  }
}
