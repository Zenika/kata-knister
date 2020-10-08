<script>
	import Nav from '@/components/Nav.svelte';

	export let segment: string;
</script>

<style lang="scss" global>
	main {
		position: relative;
		max-width: 56em;
		background-color: $primary-color;
		padding: 2em;
		margin: 0 auto;
		box-sizing: border-box;

		div {
			padding: 1em;
			background: grey;
		}
	}
</style>

<Nav {segment}/>

<main>
<div>
	<slot></slot>
</div>
</main>