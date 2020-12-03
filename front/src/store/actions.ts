import { RoomPayloadModel } from '@/models/Room';
import ApiService from '@/services/api.service';
import WsService from '@/services/ws.service';
import { ActionTree, Store } from 'vuex';
import { MutationTypes } from './mutations';
import { State } from './state';

export enum ActionTypes {
  CREATE_ROOM = 'createRoom',
  JOIN_ROOM = 'joinRoom',
  START_GAME = 'startGame',
  ROLL_DICE = 'rollDice',
  GET_GAME_STATUS = 'getGameStatus',
  GET_GRID_FOR_PLAYER = 'getGridForPlayer',
}

export type Actions = {
  [ActionTypes.CREATE_ROOM](
    store: Store<State>,
    payload: RoomPayloadModel
  ): Promise<any>;
  [ActionTypes.JOIN_ROOM](
    store: Store<State>,
    payload: any
  ): Promise<any>;
  [ActionTypes.START_GAME](store: Store<State>, payload: string): Promise<any>;
  [ActionTypes.ROLL_DICE](store: Store<State>, payload: string): Promise<any>;
  [ActionTypes.GET_GAME_STATUS](
    store: Store<State>,
    payload: string
  ): Promise<any>;
  [ActionTypes.GET_GRID_FOR_PLAYER](
    store: Store<State>,
    payload: string
  ): Promise<any>;
};

export const actions: ActionTree<State, State> = {
  async [ActionTypes.CREATE_ROOM]({ commit }, payload: RoomPayloadModel) {
    const data = await ApiService.post({ url: 'rooms', body: payload });

    if (!data) {
      return;
    }

    await WsService.send(`{ type: 'created', room: ${data._id}, playerName: ${payload.name} }`);

    commit(MutationTypes.SET_ROOM, {
      id: data._id,
      players: data.players,
      games: data.games,
    });
  },
  async [ActionTypes.JOIN_ROOM]({ commit }, { roomId, name}: any) {
    try {
      const data = await ApiService.post({ url: `rooms/${roomId}/players`, body: {name} });
      const roomData = await ApiService.get(`rooms/${roomId}`);

      await WsService.send(`{ type: 'joined', room: ${roomId}, playerName: ${name} }`);
  
      commit(MutationTypes.SET_ROOM, {
        id: roomData._id,
        players: roomData.players,
        games: roomData.games,
      });
    } catch(err) {
      return err;
    }
  },
  async [ActionTypes.START_GAME]({ commit }, id: string) {
    const response = await ApiService.post({ url: `rooms/${id}/games` });
    commit(MutationTypes.SET_GAME, response);
  },
  async [ActionTypes.ROLL_DICE]({ commit, state, dispatch }, id: string) {
    const response = await ApiService.post({ url: `rooms/${id}/games/roll` });
    await dispatch('getGameStatus', id);
    await dispatch('getGridForPlayer', {
      roomId: id,
      playerName: state.playerName,
    });
    commit(MutationTypes.SET_DICEROLL, response);
  },
  async [ActionTypes.GET_GAME_STATUS]({ commit }, id: string) {
    const response = await ApiService.get(`rooms/${id}/games/status`);
    commit(MutationTypes.SET_GAMESTATUS, response);
  },
  async [ActionTypes.GET_GRID_FOR_PLAYER]({ commit }, { roomId, playerName }) {
    const response = await ApiService.get(
      `rooms/${roomId}/games/${playerName}/grid`
    );
    commit(MutationTypes.SET_GRID, response);
  },
};
