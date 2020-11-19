export interface RoomModel {
  id: string;
  players: RoomPlayerModel[];
  games: any[];
}

export interface RoomPayloadModel {
  name: string;
}

export interface RoomPlayerModel {
  name: string;
}
