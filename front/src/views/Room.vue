<template>
  <div class="room">
    <h1>Bienvenue dans la salle {{ room._id }}</h1>

    <button type="button" @click="newGame" v-if="!game">
      Démarrer une partie
    </button>

    <button v-if="canRollDice" @click="roll">
      Lancer les dés
    </button>

    <br />

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
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
import { Game } from '@/models/Game';
import { RoomModel } from '@/models/Room';

@Options({
  computed: {
    ...mapState(['room', 'game', 'dice', 'grid']),
  },
  methods: {
    ...mapActions(['startGame', 'getGameStatus', 'rollDice']),
  },
})
export default class Room extends Vue {
  startGame!: (id: string) => void;
  rollDice!: (id: string) => Promise<any>;
  getGameStatus!: (id: string) => void;
  room!: RoomModel;
  game!: Game;
  dice!: any;
  grid!: any;

  get canRollDice() {
    return (
      this.game?.status?.playersMissing.length === 0 &&
      !this.game?.status?.gameOver
    );
  }

  async newGame() {
    await this.startGame(this.room._id);
    await this.getGameStatus(this.room._id);
  }

  async roll() {
    await this.rollDice(this.room._id);
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
