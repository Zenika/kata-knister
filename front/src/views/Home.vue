<template>
  <div class="home">
    <form v-if="!isLoggedIn" action="" @submit.prevent="logUser">
      <input type="text" v-model="name" />
      <button type="submit">Envoyer</button>
    </form>
    <div v-else>
      <h1>Bonjour {{ playerName }}</h1>

      <button type="button" @click="newRoom" v-if="!room">
        Créer une salle
      </button>

      {{ room }}
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import { mapActions, mapMutations, mapState } from 'vuex';
import { RoomPayload, RoomModel } from '@/models/Room';
import router from '../router';

@Options({
  components: {},
  computed: {
    ...mapState(['room', 'playerName']),
  },
  methods: {
    ...mapMutations(['setPlayerName']),
    ...mapActions(['createRoom']),
  },
})
export default class Home extends Vue {
  createRoom!: (data: RoomPayload) => void;
  setPlayerName!: (playerName: string) => void;
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

    this.setPlayerName(this.name);
  }

  async newRoom() {
    await this.createRoom({ name: this.name });
    router.push('room');
  }
}
</script>
