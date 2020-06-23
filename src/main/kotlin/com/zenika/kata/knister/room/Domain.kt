package com.zenika.kata.knister.room

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class Room() {
    val id: String = generateRoomId()
    val players: MutableSet<Player> = mutableSetOf()
    val currentGame : KnisterGame = KnisterGame()

    fun addPlayer(player: Player) {
        if (players.contains(player))
            throw PlayerAlreadExistsException("A player with this name already exists")

        players.add(player)
    }

    fun startGame(): KnisterGame {
        if(currentGame.started)
            throw GameAlreadyStartedException("Game already started")
        currentGame.start()
        return currentGame
    }
}

data class Player(val name: String)

//TODO : convert game into a room companion object
data class KnisterGame(val diceRolls: MutableList<Integer> = mutableListOf<Integer>(), var started : Boolean = false) {
    fun start() {
        started = true;
    }
}

fun generateRoomId(): String {
    val alphabet = ('a'..'z')
    return (1..10).map { alphabet.random() }.joinToString("")
}