import { Game } from './Game';
import { Player } from './Player';

export interface Room {
  _id: string;
  players: Player[];
  games: Game[];
}

export interface RoomPayload {
  name: string;
}
