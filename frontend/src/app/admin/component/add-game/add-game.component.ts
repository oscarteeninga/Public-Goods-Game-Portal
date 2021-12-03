import { Component, OnInit } from '@angular/core';
import { defaultGameInfo, GameInfo, InitGameInfo } from 'src/app/admin/model/game-info.model';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { GameConfigurationService } from '../../service/game-configuration.service';
import { adminPaths } from '../../../core/paths/admin.paths';
import { GameManagementService } from "../../service/game-management.service";

@Component({
  selector: 'app-add-game',
  templateUrl: './add-game.component.html',
  styleUrls: ['./add-game.component.css']
})
export class AddGameComponent implements OnInit {

  gameInfo: GameInfo = defaultGameInfo;

  submitted = false;

  constructor(private gameConfigurationService: GameConfigurationService,
              private gameManagementService: GameManagementService,
              private modalService: NgbModal,
              private router: Router,
              ) {}

  ngOnInit(): void {
    this.gameInfo = defaultGameInfo;
  }

  saveGame(): void {
    let newGameBasicInfo: InitGameInfo = {
      gameName: this.gameInfo.gameName,
      description: this.gameInfo.description,
    };

    this.gameManagementService.createGame(newGameBasicInfo)
      .subscribe(
        response => {
          console.log(response);
          this.submitted = true;
        },
        error => {
          console.log(error);
          let {message, name, ok, status, statusText, url} = error;
          // console.log(error.error.msg);
          this.openErrorInfo(error.error.msg);
        });
  }

  newGame(): void {
    this.submitted = false;
    this.gameInfo = defaultGameInfo;
  }

  goToGameList(): void {
    this.router.navigate([adminPaths.games.root.absolute]);
  }


  openErrorInfo(errorMsg: string) {
    this.modalService.open(errorMsg, {modalDialogClass: 'dark-modal'});
  }

}

