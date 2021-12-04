import {Label} from "ng2-charts";
import {ChartDataSets} from "chart.js";

export interface Charts {
  amount: Chart,
  payment: Chart,
}

export interface Chart {
  chartData: ChartDataSets[],
  chartLabels: Label[],
}

export type Round = Map<string, number>

export const exampleResults = {
  amount: {
    chartData: [
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
    ],
    chartLabels: ['1','2','3','4'],
  },
  payment: {
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
}
