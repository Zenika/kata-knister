package com.zenika.kata.knister.room

import Grid
import kotlin.math.round

class Room() {
    val _id: String = generateRoomId()
    val players: MutableSet<Player> = mutableSetOf()
    val games : MutableList<KnisterGame> = mutableListOf()

    fun addPlayer(player: Player) {
        if (players.contains(player))
            throw PlayerAlreadExistsException("A player with this name already exists")

        players.add(player)
    }

    fun currentGame(): KnisterGame {
        check(games.isNotEmpty())
        return games.last()
    }
    fun startGame(): KnisterGame {
        val currentGame = games.lastOrNull()
        if(currentGame != null && !currentGame.isOver()) {
            throw GameAlreadyStartedException("partie en cours")
        }
        val newGame = KnisterGame(players.toSet())
        games.add(newGame)
        return newGame
    }

}

data class Player(val name: String)

class KnisterGame(val players : Set<Player>, val diceRolls: MutableList<DiceRoll> = mutableListOf<DiceRoll>()) {
    val gridsForPlayers = players.map { it to Grid() }.toMap()
    init {
        check(players.isNotEmpty())
    }

    fun rollDices(): DiceRoll {
        check(roundOver())
        check(diceRolls.size < 25)
        val diceRoll = rollDicePair()
        diceRolls.add(diceRoll)
        return diceRoll
    }

    private fun roundOver(): Boolean {
        return gridsForPlayers.values.all { it.dicesPlaced() == diceRolls.size }
    }

    fun playerPlacesDicesInSquare(player : Player, x: Int, y: Int) {
        gridsForPlayers[player]!!.placeDices(x, y, diceRolls.last().score())
    }

    fun isOver(): Boolean {
        return diceRolls.size == 25 && roundOver()
    }
}

data class DiceRoll(val first : Dice, val second : Dice) {
    fun score() : Int {
        return first.roll+second.roll
    }
}

data class Dice(val roll : Int) {
    init {
        check(roll in 1..6)
    }
}

fun generateRoomId(): String {
    val alphabet = ('a'..'z')
    return (1..10).map { alphabet.random() }.joinToString("")
}

fun rollDicePair() : DiceRoll {
    return DiceRoll(rollDice(), rollDice())
}

fun rollDice() : Dice {
    return Dice((1..6).random())
}
