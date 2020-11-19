import { Game } from '@/models/Game';
import { RoomModel, RoomPayload } from '@/models/Room';
import ApiService from '@/services/api.service';
import { createLogger, createStore } from 'vuex';
import VuexPersistence from 'vuex-persist';

const vuexLocal = new VuexPersistence({
  storage: window.localStorage,
});

export default createStore({
  plugins: [createLogger(), vuexLocal.plugin],
  state: {
    playerName: null,
    room: null as RoomModel | null,
    game: null as Game | null,
    dice: {},
    grid: null,
  },
  getters: {},
  mutations: {
    setPlayerName(state, data) {
      state.playerName = data;
    },
    setRoom(state, data) {
      state.room = data;
    },
    setGame(state, data) {
      state.game = data;
    },
    setGameStatus(state, data) {
      if (state.game) {
        state.game.status = data;
      }
    },
    setDice(state, data) {
      state.dice = data;
    },
    setGrid(state, data) {
      state.grid = data;
    },
  },
  actions: {
    async createRoom({ commit }, data: RoomPayload) {
      const response = await ApiService.post({ url: 'rooms', body: data });
      commit('setRoom', response);
    },
    async startGame({ commit, dispatch, state }, id: string) {
      const response = await ApiService.post({ url: `rooms/${id}/games` });
      await dispatch('getGridForPlayer', {
        roomId: id,
        playerName: state.playerName,
      });
      commit('setGame', response);
    },
    async rollDice({ commit, state, dispatch }, id: string) {
      const response = await ApiService.post({ url: `rooms/${id}/games/roll` });
      await dispatch('getGameStatus', id);
      await dispatch('getGridForPlayer', {
        roomId: id,
        playerName: state.playerName,
      });
      commit('setDice', response);
    },
    async getGameStatus({ commit }, id: string) {
      const response = await ApiService.get(`rooms/${id}/games/status`);
      commit('setGameStatus', response);
    },
    async getGridForPlayer({ commit }, { roomId, playerName }) {
      const response = await ApiService.get(
        `rooms/${roomId}/games/${playerName}/grid`
      );
      commit('setGrid', response);
    },
  },
  modules: {},
});
