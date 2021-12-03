import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { GameSessionToken } from "../../../core/model/game-session-token.model";
import { UserStateService } from "../../service/user-state.service";

@Component({
  selector: 'app-game-connection',
  templateUrl: './game-connection.component.html',
  styleUrls: ['./game-connection.component.css']
})
export class GameConnectionComponent implements OnInit {

  private readonly gameSessionToken: GameSessionToken;
  private readonly returnUrl: string;

  constructor(private userStateService: UserStateService,
              private route: ActivatedRoute,
              private router: Router) {
    this.gameSessionToken = GameSessionToken.from(route.snapshot.params.id);
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  ngOnInit(): void {
    this.userStateService
      .ensureConnected(this.gameSessionToken)
      .subscribe(
        connected => {
          if (connected) {
            this.router.navigate([this.returnUrl]);
          } else {
            console.error('Could not connect user to game');
          }
        },
        error => console.error(error)
      );
  }
}
