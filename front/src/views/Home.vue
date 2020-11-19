<template>
  <div class="home">
    <form v-if="!isLoggedIn" action="" @submit.prevent="logUser">
      <input type="text" v-model="name" />
      <button type="submit">Envoyer</button>
    </form>
    <div v-else>
      <h1>Bonjour {{ playerName }}</h1>

      <button type="button" @click="newRoom" v-if="!room.id">
        Créer une salle
      </button>

      {{ room }}
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import { mapState } from 'vuex';
import { RoomModel } from '@/models/Room';
import router from '../router';
import { MutationTypes } from '@/store/mutations';
import { ActionTypes } from '@/store/actions';

@Options({
  computed: {
    ...mapState(['room', 'playerName']),
  },
})
export default class Home extends Vue {
  room!: RoomModel;
  playerName!: string;
  name = '';

  get isLoggedIn() {
    return !!this.playerName;
  }

  logUser() {
    if (!this.name) {
      return;
    }

    this.$store.commit(MutationTypes.SET_PLAYERNAME, this.name);
  }

  async newRoom() {
    await this.$store.dispatch(ActionTypes.CREATE_ROOM, { name: this.name });
    router.push('room');
  }
}
</script>
