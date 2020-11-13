<template>
  <div class="wrapper">
    <div>
      <img src="@/assets/logo_white.svg" alt="" />
      <router-view />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  margin: 0 auto;
  width: 94%;
  max-width: 112rem;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;

  > div {
    width: 100%;
  }
}

img {
  display: block;
  max-width: 40rem;
  margin: 0 auto;
}
</style>
