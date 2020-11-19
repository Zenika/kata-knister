import { DiceRollModel } from '@/models/Dice';
import { GameModel } from '@/models/Game';
import { GridModel } from '@/models/Grid';
import { RoomModel } from '@/models/Room';

export const state = {
  playerName: '',
  room: {} as RoomModel,
  game: {} as GameModel,
  diceRoll: {} as DiceRollModel,
  grid: null as GridModel | null,
};

export type State = typeof state;
