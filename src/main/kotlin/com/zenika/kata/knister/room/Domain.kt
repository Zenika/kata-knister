package com.zenika.kata.knister.room

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
        if(gameAlreadyStarted()) {
            throw GameAlreadyStartedException("Game already started")
        }
        val newGame = KnisterGame()
        games.add(newGame)
        newGame.start()
        return newGame
    }

    private fun gameAlreadyStarted(): Boolean {
        return games.lastOrNull()?.started?:false
    }
}

data class Player(val name: String)

data class KnisterGame(val diceRolls: MutableList<DiceRoll> = mutableListOf<DiceRoll>(), var started : Boolean = false) {
    fun start() {
        started = true;
    }

    fun rollDices(): DiceRoll {
        check(started)
        check(diceRolls.size < 25)
        val diceRoll = rollDicePair()
        diceRolls.add(diceRoll)
        return diceRoll
    }
}

data class DiceRoll(val first : Dice, val second : Dice)

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
