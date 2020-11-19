import { DiceRollModel } from '@/models/Dice';
import { GameModel, GameStatusModel } from '@/models/Game';
import { GridModel } from '@/models/Grid';
import { RoomModel } from '@/models/Room';
import { MutationTree } from 'vuex';
import { State } from './state';

export enum MutationTypes {
  SET_PLAYERNAME = 'setPlayerName',
  SET_ROOM = 'setRoom',
  SET_GAME = 'setGame',
  SET_GAMESTATUS = 'setGameStatus',
  SET_DICEROLL = 'setDiceRoll',
  SET_GRID = 'setGrid',
}

export type Mutations = {
  [MutationTypes.SET_PLAYERNAME](state: State, data: string): void;
  [MutationTypes.SET_ROOM](state: State, data: RoomModel): void;
  [MutationTypes.SET_GAME](state: State, data: GameModel): void;
  [MutationTypes.SET_GAMESTATUS](state: State, data: GameStatusModel): void;
  [MutationTypes.SET_DICEROLL](state: State, data: DiceRollModel): void;
  [MutationTypes.SET_GRID](state: State, data: GridModel): void;
};

export const mutations: MutationTree<State> = {
  [MutationTypes.SET_PLAYERNAME](state: State, data: string): void {
    state.playerName = data;
  },
  [MutationTypes.SET_ROOM](state: State, data: RoomModel): void {
    state.room = data;
  },
  [MutationTypes.SET_GAME](state: State, data: GameModel): void {
    state.game = data;
  },
  [MutationTypes.SET_GAMESTATUS](state: State, data: GameStatusModel): void {
    if (data.playersMissing) {
      state.game.status = data;
    }
  },
  [MutationTypes.SET_DICEROLL](state: State, data: DiceRollModel): void {
    state.diceRoll = data;
  },
  [MutationTypes.SET_GRID](state: State, data: GridModel): void {
    state.grid = data;
  },
};
