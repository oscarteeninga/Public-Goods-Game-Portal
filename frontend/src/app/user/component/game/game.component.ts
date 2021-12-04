import {Component, OnInit} from '@angular/core';
import {GameProgressService} from '../../service/game-progress.service';
import {defaultGameProgress, GameProgress} from '../../model/game-progress.model';
import {defaultGameState, GameRoundSummary, GameState} from '../../model/game-state.model';
import {GameSessionToken} from '../../../core/model/game-session-token.model';
import {ActivatedRoute, Router} from '@angular/router';
import {GameStatus} from '../../../core/model/game-status';
import {PlayerDecision} from '../../model/player-decision.model';
import {PlayerDecisionOutcome} from '../../model/player-decision-outcome.model';
import {UserStateService} from "../../service/user-state.service";
import {userPaths} from "../../../core/paths/user.paths";
import {Chart, Round} from "../../model/charts.model";
import {ChartDataSets} from "chart.js";
import {Label} from "ng2-charts";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {

  private readonly gameStatePollInterval: number = 1000;
  readonly roundColumnName: string = 'round';
  readonly payoffColumnName: string = 'payoff';

  gameSessionToken: GameSessionToken;
  walletState: number = 0;
  alreadyDecided: boolean = false;
  currentContribution: number = 0;

  gameProgress: GameProgress = defaultGameProgress;
  gameState: GameState = defaultGameState;

  historyTableColumns: string[] = [];
  historyTableData: Round[] = [];

  charts: Chart[] = []

  chartOptions = {
    responsive: true
  }

  constructor(private userStateService: UserStateService,
              private gameProgressService: GameProgressService,
              private route: ActivatedRoute,
              private router: Router) {
    this.gameSessionToken = GameSessionToken.from(route.snapshot.params.id);
  }

  ngOnInit(): void {
    if (this.userStateService.isUserRegistered() && this.userStateService.isUserConnected(this.gameSessionToken)) {
      setInterval(() => {
        this.checkGameProgress();
      }, this.gameStatePollInterval);
    } else {
      this.goToGameConnection();
    }
  }

  isGameActive(): boolean {
    return this.gameProgress.status === GameStatus.Active;
  }

  sendDecision(): void {
    let playerDecision: PlayerDecision = {
      contribution: this.currentContribution,
      roundNum: this.gameProgress.round,
      username: this.getUsername(),
    };

    this.gameProgressService.sendDecision(this.gameSessionToken, playerDecision).subscribe(
      decision => {
        switch (decision) {
          case PlayerDecisionOutcome.Accepted:
            console.log('Decision accepted.');
            this.checkGameState();
            break;
          case PlayerDecisionOutcome.RejectedAlreadyDecided:
            console.warn('Decision rejected - player already decided.');
            this.checkGameProgress();
            break;
          case PlayerDecisionOutcome.RejectedBadRound:
            console.warn('Decision rejected - sent for wrong round.');
            this.checkGameProgress();
            break;
          case PlayerDecisionOutcome.RejectedBadValue:
            console.warn('Decision rejected - bad contribution value.');
            break;
          case PlayerDecisionOutcome.RejectedGameNotActive:
            console.warn('Decision rejected - game not active.');
            this.checkGameProgress();
            break;
          case PlayerDecisionOutcome.RejectedPlayerNotConnected:
            console.warn('Decision rejected - player not connected to game');
            this.goToGameConnection();
            break;
          case PlayerDecisionOutcome.RejectedPlayerNotRegistered:
            console.warn('Decision rejected - player not registered.');
            this.goToGameConnection();
            break;
        }
      }, error => {
        console.error(error);
      }
    )
  }

  getUsername(): string {
    return this.userStateService.getUser().username;
  }

  downloadChartsAsPDF(): void {

  }

  generatePayoffChart(): Chart {
    let payOffs: number[] = []
    let rounds: Label[] = Array.from(this.gameState.gameHistory.keys()).map(r => (r + 1).toString())
    this.gameState.gameHistory.forEach((round) => {
      payOffs.push(round.payoff)
    })

    return {
      chartData: [{
        data: payOffs,
        label: "Payoff"
      }],
      chartLabels: rounds,
    }
  }

  generatePlayersChart(): Chart {
    let rounds: Label[] = Array.from(this.gameState.gameHistory.keys()).map(r => (r + 1).toString())

    let names: string[] = Object.keys(this.gameState.gameHistory[0].contributions)
    let dataFormatted = new Map<string, number[]>()

    names.forEach((name: string) => {
      this.gameState.gameHistory.forEach((round: GameRoundSummary) => {
        let actualValues = dataFormatted.get(name) || []
        let contributors = new Map(Object.entries(round.contributions))
        let value = contributors.get(name) || 0.0
        actualValues.push(value)
        dataFormatted.set(name, actualValues)
      })
    })

    let chartData: ChartDataSets[] = []
    dataFormatted.forEach((values, name) => {
      chartData.push({
        data: values,
        label: name,
      })
    })


    return {
      chartData: chartData,
      chartLabels: rounds,
    }
  }

  generateCharts() {
    this.charts = [this.generatePayoffChart(), this.generatePlayersChart()]
  }

  private checkGameProgress(): void {
    this.gameProgressService.getProgress(this.gameSessionToken).subscribe(
      newGameProgress => {
        console.log(newGameProgress);
        this.handleProgress(newGameProgress);
      },
      error => {
        console.error(error);
      }
    );
  }

  private handleProgress(newGameProgress: GameProgress): void {
    if (this.hasGameProgressChanged(newGameProgress)) {
      this.checkGameState();
    }
    this.gameProgress = newGameProgress;
  }

  private hasGameProgressChanged(newGameProgress: GameProgress): boolean {
    return this.gameProgress.status != newGameProgress.status
      || this.gameProgress.round != newGameProgress.round;
  }

  private checkGameState(): void {
    this.gameProgressService.getState(this.gameSessionToken).subscribe(
      newGameState => {
        console.log(newGameState);
        this.handleGameState(newGameState);
      },
      error => {
        console.error(error);
      }
    );
  }

  private handleGameState(newGameState: GameState): void {
    this.gameState = newGameState;
    let username = this.getUsername();
    this.walletState = new Map(Object.entries(this.gameState.walletsState)).get(username) || 0;
    this.alreadyDecided = new Set(this.gameState.playersThatDecided).has(username);
    this.recreateTableData();
  }

  private recreateTableData(): void {
    // create row data
    this.historyTableData = this.gameState.gameHistory.map(
      (gameRoundSummary, index) => {
        return new Map([
          [this.roundColumnName, index + 1],
          ...Object.entries(gameRoundSummary.contributions),
          [this.payoffColumnName, gameRoundSummary.payoff],
        ]);
      }
    ).reverse();

    // create column names
    let allPlayerNames: Set<string> = this.gameState.gameHistory.map(
      gameRoundSummary => {
        return new Set(new Map(Object.entries(gameRoundSummary.contributions)).keys());
      }
    ).reduce(
      (acc, nameSet) => {
        return new Set([...acc, ...nameSet]);
      }, new Set()
    );

    this.historyTableColumns = [
      this.roundColumnName,
      ...Array.from(allPlayerNames).sort(),
      this.payoffColumnName
    ];

    this.generateCharts()
  }

  private goToGameConnection(): void {
    let targetPath = `${userPaths.connect.absolute}/${this.gameSessionToken}`;
    let returnPath = '/' + this.route.snapshot.url.join('/');
    this.router.navigate([targetPath], {queryParams: {returnUrl: returnPath}});
  }
}
