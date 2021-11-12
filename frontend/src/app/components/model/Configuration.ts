export interface Configuration {
  rounds: number,
  multiplier: number,
  players: {
    freeriders: number,
    cooperators: number,
    casuals: number,
  }
}
