package com.zenika.kata.knister.room

import Grid
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
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

    private val roundsNumber = 25

    fun rollDices(): DiceRoll {
        check(roundOver())
        check(diceRolls.size < roundsNumber)
        val diceRoll = rollDicePair()
        diceRolls.add(diceRoll)
        return diceRoll
    }

    private fun roundOver(): Boolean {
        return gridsForPlayers.values.all { it.dicesPlaced() == diceRolls.size }
    }

    fun playerPlacesDicesInSquare(player : Player, gridPosition: GridPosition) {
        val playerGrid = gridsForPlayers.getOrElse(player) { throw IllegalArgumentException("non existing player") }
        if(playerGrid.dicesPlaced() == diceRolls.size) { throw IllegalStateException("diceRoll already placed for player ${player.name}") }
        playerGrid.placeDices(gridPosition, diceRolls.last().score())
    }

    fun isOver(): Boolean {
        return diceRolls.size == roundsNumber && roundOver()
    }

    fun score(player: Player): Int {
        check(isOver())
        val grid = gridsForPlayers.getOrElse(player) { throw IllegalArgumentException("non existing player") }
        return grid.score()
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

data class GridPosition(val x: Int, val y: Int);

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
