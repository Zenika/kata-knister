<template>
  <div class="room">
    <h1>Bienvenue dans la salle {{ room.id }}</h1>

    <button type="button" @click="newGame" v-if="!game.running">
      Démarrer une partie
    </button>

    <button v-if="canRollDice" @click="roll">
      Lancer les dés
    </button>

    <br />

    <button @click="startPolling">Start polling</button>
    <button @click="stopPolling">Stop polling</button>

    <div v-if="grid?.lines">
      <div class="line" v-for="(line, index) in grid.lines" :key="index">
        <span class="cell" v-for="(cell, cellIndex) in line" :key="cellIndex">
          {{ cell }}
        </span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import { mapState } from 'vuex';
import { GameModel } from '@/models/Game';
import { RoomModel } from '@/models/Room';
import { DiceRollModel } from '@/models/Dice';
import { GridModel } from '@/models/Grid';
import { ActionTypes } from '@/store/actions';

enum WebSocketEvents {
  Created = 'created',

}

@Options({
  computed: {
    ...mapState(['room', 'game', 'dice', 'grid']),
  },
})
export default class Room extends Vue {
  room!: RoomModel;
  game!: GameModel;
  diceRoll!: DiceRollModel;
  grid!: GridModel;
  polling!: number;
  isPolling = false;
  websocket: WebSocket = new WebSocket("ws://34.78.67.84:8080/roomSocket");;

  created() {
    this.websocket.onmessage = this.onMessage;

    this.websocket.onopen = () => {
      this.websocket.send(`{ type: 'created', roomId: ${this.room.id}, playerName: ${this.$store.state.playerName} }`);
    };
  }

  onMessage(event: any) {
    console.log(event);
  }

  get canRollDice() {
    return (
      this.game.status?.playersMissing?.length === 0 &&
            !this.game.status?.gameOver
    );
  }

  async newGame() {
    await this.$store.dispatch(ActionTypes.START_GAME, this.room.id);
    await this.$store.dispatch(ActionTypes.GET_GAME_STATUS, this.room.id);
  }

  async roll() {
    await this.$store.dispatch(ActionTypes.ROLL_DICE, this.room.id);
  }

  beforeUnmount() {
    if (this.isPolling) {
      this.stopPolling();
    }
  }

  stopPolling() {
    clearInterval(this.polling);
    this.isPolling = false;
  }

  startPolling() {
    this.isPolling = true;
    this.polling = setInterval(() => {
      this.$store.dispatch(ActionTypes.GET_GAME_STATUS, this.room.id);
    }, 1000);
  }
}
</script>

<style lang="scss" scoped>
.room {
  --grid-size: 80px;
}

.line {
  height: var(--grid-size);
}
.cell {
  line-height: var(--grid-size);
  width: var(--grid-size);
  height: var(--grid-size);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 0 0 1px white;
  margin: 0;
}
</style>
