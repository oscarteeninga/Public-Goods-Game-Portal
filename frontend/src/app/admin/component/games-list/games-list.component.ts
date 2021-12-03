import { Component, OnInit } from '@angular/core';
import { defaultGameInfo, GameInfo } from '../../model/game-info.model';
import { GameFindingService } from '../../service/game-finding.service';
import { adminPaths } from '../../../core/paths/admin.paths';
import { ConfigureEditState } from '../../../core/model/button-states';
import { GameStatus } from '../../../core/model/game-status';
import { GameManagementService } from "../../service/game-management.service";

@Component({
  selector: 'app-games-list',
  templateUrl: './games-list.component.html',
  styleUrls: ['./games-list.component.css']
})
export class GamesListComponent implements OnInit {

  readonly adminPaths = adminPaths;

  games?: GameInfo[];

  GameStatus = GameStatus;

  currentGame: GameInfo = defaultGameInfo;
  currentIndex: number = -1;
  currentConfigureButtonName = ConfigureEditState.Configure;

  searchStringVal: string = '';

  constructor(private gameInformationService: GameFindingService,
              private gameManagementService: GameManagementService,) {
  }

  ngOnInit(): void {
    this.retrieveGames();
  }

  retrieveGames(): void {
    this.gameInformationService.getAllGames()
      .subscribe(
        data => {
          this.games = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  refreshList(): void {
    this.retrieveGames();
    this.currentGame = defaultGameInfo;
    this.currentIndex = -1;
    this.setConfigureBtnName();
  }

  setActiveGame(gameInfo: GameInfo, index: number): void {
    this.currentGame = gameInfo;
    this.currentIndex = index;
    if (gameInfo.status != GameStatus.Created)
      this.setEditBtnName();
    else
      this.setConfigureBtnName();
  }

  removeAllGames(): void {
    // this.singHTTPService.deleteAllGames()
    //   .subscribe(
    //     response => {
    //       console.log(response);
    //       this.refreshList();
    //     },
    //     error => {
    //       console.log(error);
    //     });
  }

  searchGames(): void {
    this.currentGame = defaultGameInfo;
    this.currentIndex = -1;

    if (this.searchStringVal.length > 0) {
      this.gameInformationService.findGamesContainingText(this.searchStringVal)
        .subscribe(
          data => {
            this.games = data;
            console.log(data);
          },
          error => {
            console.log(error);
          });
    } else {
      this.refreshList();
    }
  }

  startGame(): void {
    this.gameManagementService.activateAndStartGame(this.currentGame.gameSessionToken).subscribe(
      data => {
        this.currentGame = data;
        console.log(data);
      },
      error => {
        console.log(error);
      });
    window.location.reload();
  }

  private setConfigureBtnName() {
    this.currentConfigureButtonName = ConfigureEditState.Configure;
  }

  private setEditBtnName() {
    this.currentConfigureButtonName = ConfigureEditState.Edit;
  }
}

