import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GameConfiguration } from '../model/game-configuration.model';
import { GameSessionToken } from '../../core/model/game-session-token.model';
import { GameConfigurationRest } from "../rest/game-configuration.rest";

@Injectable({
  providedIn: 'root'
})
export class GameConfigurationService {

  constructor(private gameConfigurationRest: GameConfigurationRest) {
  }

  configureGame(gameSessionToken: GameSessionToken, gameConf: GameConfiguration): Observable<GameConfiguration> {
    return this.gameConfigurationRest.configureGame(gameSessionToken, gameConf);
  }

  getGameConfiguration(gameSessionToken: GameSessionToken): Observable<GameConfiguration> {
    return this.gameConfigurationRest.getGameConfiguration(gameSessionToken);
  }
}
