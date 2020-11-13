import { RoomPayload } from '@/models/Room';
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
    room: null,
  },
  mutations: {
    setPlayerName(state, data) {
      state.playerName = data;
    },
    setRoom(state, data) {
      state.room = data;
    },
  },
  actions: {
    async createRoom({ commit }, data: RoomPayload) {
      const response = await ApiService.post({ url: 'rooms', body: data });
      commit('setRoom', response);
    },
  },
  modules: {},
});
