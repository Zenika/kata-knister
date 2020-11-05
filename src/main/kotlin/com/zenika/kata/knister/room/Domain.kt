package com.zenika.kata.knister.room

import Grid

class Room() {
    val _id: String = generateRoomId()
    val players: MutableSet<Player> = mutableSetOf()
    val games : MutableList<KnisterGame> = mutableListOf()

    fun addPlayer(player: Player) {
        if (players.contains(player))
            throw PlayerAlreadExistsException("A player with this name already exists")

        players.add(player)
    }

    fun removePlayer(player: Player) {
        players.remove(player)
        val currentGame = games.lastOrNull()
        if(currentGame != null && currentGame.isRunning()) {
            currentGame.removePlayer(player)
        }
    }

    fun currentGame(): KnisterGame {
        check(games.isNotEmpty())
        return games.last()
    }
    fun startGame(): KnisterGame {
        if(games.lastOrNull()?.isRunning() ?: false) {
            throw GameAlreadyStartedException("partie en cours")
        }
        val newGame = KnisterGame(players.toSet())
        games.add(newGame)
        return newGame
    }


}

data class Player(val name: String) {
    override fun toString(): String {
        return name;
    }
}

class KnisterGame(var gridsForPlayers : MutableMap<Player, Grid>, val diceRolls: MutableList<DiceRoll> = mutableListOf<DiceRoll>()) {
    constructor(players : Set<Player> = mutableSetOf()) : this(players.map { it to Grid() }.toMap().toMutableMap()) {

    }
    var cancelled = false
    init {
        check(gridsForPlayers.isNotEmpty())
    }

    private val roundsNumber = 25

    fun rollDices(): DiceRoll {
        check(roundOver())
        check(!isOver())
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

    private fun isOver(): Boolean {
        return diceRolls.size == roundsNumber && roundOver()
    }

    fun isRunning(): Boolean {
        return !cancelled && !isOver()
    }

    fun score(player: Player): Int {
        check(isOver())
        val grid = gridsForPlayers.getOrElse(player) { throw IllegalArgumentException("non existing player") }
        return grid.score()
    }

    fun removePlayer(player: Player) {
        val remove = gridsForPlayers.remove(player)
        if(remove == null) {
            throw IllegalArgumentException("non existing player")
        }
        if(gridsForPlayers.isEmpty()) {
            cancelled = true
        }
    }

    fun scores(): Map<Player, Int> {
        check(isOver())
        return gridsForPlayers.mapValues { it.value.score() }
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

data class GridPosition(val x: Int, val y: Int)

fun generateRoomId(): String {
    val alphabet = ('a'..'z')
    return (1..10).map { alphabet.random() }.joinToString("")
}

fun rollDicePair() : DiceRoll {
    return DiceRoll(rollDice(), rollDice())
}

//TODO : avoir un service pour lancer les dés pour pouvoir simuler le random pour les tests et/ou appeler un api externe
fun rollDice() : Dice {
    return Dice((1..6).random())
}
