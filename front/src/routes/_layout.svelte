<script>
	import Nav from '@/components/Nav.svelte';

	export let segment: string;
</script>

<style lang="scss" global>
	main {
		position: relative;
		max-width: 56em;
		padding: 2em;
		margin: 0 auto;
		box-sizing: border-box;
	}
</style>

<main>
	<slot></slot>
</main>