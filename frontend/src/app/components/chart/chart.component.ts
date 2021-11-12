import {Component, OnInit} from '@angular/core';
import {ConfigService} from "../../service/config.service";
import {Configuration} from "../model/Configuration";

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit {

  configuration: Configuration = {
    rounds: 5,
    multiplier: 2,
    players: {
      freeriders: 1,
      cooperators: 1,
      casuals: 1,
    }
  }

  freeRiders = 1
  cooperators = 1
  casuals = 1

  multiplier = 2.0

  showProgressBar = false

  chartData: any
  chartLabels: any
  chartOptions: any

  constructor(private configService: ConfigService) {
    this.chartData = [
      {
        data: [330, 600, 260, 700],
        label: 'Account A'
      },
      {
        data: [120, 455, 100, 340],
        label: 'Account B'
      },
      {
        data: [45, 67, 800, 500],
        label: 'Account C'
      }
    ];

    this.chartLabels = [1,2,3,4];

    this.chartOptions = {
      responsive: true
    };
  }

  ngOnInit(): void {
  }

  getConfig(): Configuration {
    return this.configuration
  }

  runSimulation(): void {
    this.configService.getSimulationChartData(this.getConfig())
    this.showProgressBar = true
  }
}
