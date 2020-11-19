import { Game } from './Game';
import { Player } from './Player';

export interface RoomModel {
  _id: string;
  players: Player[];
  games: Game[];
}

export interface RoomPayload {
  name: string;
}
