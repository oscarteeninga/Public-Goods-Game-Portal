import {Component, OnInit} from '@angular/core';
import {ConfigService} from "../../service/config.service";
import {Configuration} from "../model/Configuration";
import {ChartResult, ChartResults, exampleResults} from "../model/ChartResults";

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

  showProgressBar = false

  charts: ChartResults | undefined

  actualChart: ChartResult | undefined

  chartOptions = {
    responsive: true

  }

  constructor(private configService: ConfigService) {
  }

  ngOnInit(): void {
  }

  getConfig(): Configuration {
    return this.configuration
  }

  runSimulation(): void {
    this.configService.getSimulationChartData(this.getConfig())
    this.showProgressBar = true
    this.charts = exampleResults
    this.actualChart = this.charts.amount
  }
}
