<template>
  <button :type="type"><slot></slot></button>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

@Options({
  props: {
    type: String,
    default: 'button',
  },
})
export default class Button extends Vue {
  type!: string;
}
</script>

<style lang="scss" scoped></style>
