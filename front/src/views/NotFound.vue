<template>
  <div>
    <h1>4<img src="@/assets/icon.svg" />4</h1>
    <p>
      Page non trouvée, <router-link to="/">Revenir à l'accueil</router-link>
    </p>
  </div>
</template>

<style scoped>
h1 {
  margin: 0;
  font-size: 40rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
img {
  width: 0.8em;
  height: 0.8em;
  display: block;
}

p {
  text-align: center;
}
</style>
