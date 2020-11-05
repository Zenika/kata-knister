<style>
</style>

<svelte:head>
	<title>Knister</title>
</svelte:head>

{#if playerName}
	<h1>Bonjour {playerName}</h1>

	<button on:click={createRoom} type="button">Créer une salle 🤘</button>
{:else}
	<form on:submit|preventDefault={connect}>
		<input type="text" name="playerName" bind:value={inputPlayerName} placeholder="Entrer un nom...">
		<button type="submit">Enregistrer</button>
	</form>
{/if}

<script lang="ts">
import { onMount } from 'svelte';

let playerName: string = '';
let inputPlayerName: string;

onMount(() => {
	playerName = window.localStorage.getItem('playerName');
});

function connect(e) {
	window.localStorage.setItem('playerName', inputPlayerName);
	playerName = inputPlayerName;
}

async function createRoom() {
const req = await fetch('http://localhost:8080/rooms', {
	method: 'POST',
	headers: {
      'Content-Type': 'application/json'
    },
	body: JSON.stringify({
		name: playerName
	})
});

console.log(req);
}
</script>