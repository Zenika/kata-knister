<template>
  <div class="home">
    <form v-if="!isLoggedIn" action="" @submit.prevent="logUser">
      <input type="text" v-model="name" />
      <button type="submit">Envoyer</button>
    </form>
    <div v-else>
      <h1>Bonjour {{ name }}</h1>

      <button type="button" @click="createRoom">Créer une salle</button>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
})
export default class Home extends Vue {
  isLoggedIn = false;
  name = "";

  mounted() {
    this.isLoggedIn = !!localStorage.getItem("user");
    this.name = localStorage.getItem("user") || "";
  }

  logUser() {
    if (!this.name) {
      return;
    }

    localStorage.setItem("user", this.name.trim());
    this.isLoggedIn = true;
  }

  async createRoom() {
    const req = await fetch("http://34.78.67.84:8080/rooms", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: this.name,
      }),
    });

    console.log(req);
  }
}
</script>
