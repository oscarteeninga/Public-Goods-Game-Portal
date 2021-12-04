import {Label} from "ng2-charts";
import {ChartDataSets} from "chart.js";

export interface Chart {
  chartData: ChartDataSets[],
  chartLabels: Label[],
}

export type Round = Map<string, number>

export const exampleResults = {
  chartData: [
    {
      data: [12, 32, 54, 65],
      label: 'Account A'
    },
    {
      data: [43, 45, 32, 23],
      label: 'Account B'
    },
    {
      data: [12, 32, 43, 3],
      label: 'Account C'
    }
  ],
  chartLabels: ['10','20','30','40'],
}
