import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { defaultGameConfiguration, GameConfiguration } from '../../model/game-configuration.model';
import { GameConfigurationService } from '../../service/game-configuration.service';
import { adminPaths } from '../../../core/paths/admin.paths';
import { GameSessionToken } from '../../../core/model/game-session-token.model';
import { GameManagementService } from "../../service/game-management.service";

@Component({
  selector: 'app-game-configuration',
  templateUrl: './game-configuration.component.html',
  styleUrls: ['./game-configuration.component.css']
})
export class GameConfigurationComponent implements OnInit {

  prevGameName: string = '';

  currentGameConf: GameConfiguration = defaultGameConfiguration;


  constructor(
    private gameManagementService: GameManagementService,
    private gameConfigurationService: GameConfigurationService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.getGameConf(this.route.snapshot.params.id);
  }


  getGameConf(gameSessionToken: GameSessionToken): void {
    this.gameConfigurationService.getGameConfiguration(gameSessionToken)
      .subscribe(
        data => {
          this.currentGameConf = data;
          this.prevGameName = String(this.currentGameConf.gameName);
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  updateGameConf(): void {
    this.gameConfigurationService.configureGame(this.currentGameConf.gameSessionToken, this.currentGameConf)
      .subscribe(
        responseData => {
          this.currentGameConf = responseData;
          console.log(responseData);
          this.router.navigate([adminPaths.games.list.absolute]);
        },
        error => {
          console.log(error);
        });
  }

  deleteGame(): void {
    this.gameManagementService.deleteGame(this.currentGameConf.gameSessionToken)
      .subscribe(
        response => {
          console.log(response);
          this.router.navigate([adminPaths.games.list.absolute]);
        },
        error => {
          console.log(error);
        });
  }

}

