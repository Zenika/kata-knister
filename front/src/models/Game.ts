export interface Game {
  id: number;
  gridForPlayers?: any;
  status?: GameStatusModel;
}

export interface GameStatusModel {
  gameOver: boolean;
  playersMissing: string[];
  remainingRounds: number;
}
