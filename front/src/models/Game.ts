export interface GameModel {
  cancelled: boolean;
  running: boolean;
  gridForPlayers: any;
  diceRolls: [];
  status?: GameStatusModel;
}

export interface GameStatusModel {
  gameOver: boolean;
  playersMissing: string[];
  remainingRounds: number;
}
