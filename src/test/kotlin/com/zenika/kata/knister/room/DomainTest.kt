package com.zenika.kata.knister.room

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class DomainTest {
    @Nested
    inner class RoomIdGeneratorTest {

        @Test
        fun `generated id is not empty`() {
            val id = generateRoomId()

            assertThat(id).isNotEmpty()
        }

        @Test
        fun `generated id is size of 10`() {
            val id = generateRoomId()

            assertThat(id).hasSize(10)
        }

        @Test
        fun `two generated id are differents`() {
            val id1 = generateRoomId()
            val id2 = generateRoomId()

            assertThat(id1).isNotEqualTo(id2)
        }
    }

    @Nested
    inner class RoomDataTest {
        @Test
        fun `add same player twice generates a business exception`() {
            val myRoom = Room()
            myRoom.addPlayer(Player("toto"))

            assertThatThrownBy { myRoom.addPlayer(Player("toto")) }.isInstanceOf(PlayerAlreadExistsException::class.java)
        }

        @Test
        fun `start a room already started generates a business exception`() {
            val myRoom = Room()
            myRoom.addPlayer(Player("toto"))
            myRoom.startGame()

            assertThatThrownBy { myRoom.startGame() }.isInstanceOf(GameAlreadyStartedException::class.java)
        }

        // TODO : can we start a game if there is no player
    }

    @Nested
    inner class GameTest {
        @Test
        fun `game allows dice rolls on started game` () {
            val game = KnisterGame(setOf(Player("toto")))

            val dices = game.rollDices()

            assertThat(game.diceRolls).hasSize(1)
            assertThat(dices).isEqualTo(game.diceRolls[0])
        }

        @Test
        fun `game can start only if list of players is not empty` () {
            assertThatThrownBy { KnisterGame(emptySet()) }.isInstanceOf(IllegalStateException::class.java)
        }

        @Test
        fun `game allows dice rolls only if round is over` () {
            val game = KnisterGame(setOf(Player("toto")))
            game.rollDices()

            assertThatThrownBy { game.rollDices() }.isInstanceOf(IllegalStateException::class.java)
        }

        @Test
        fun `game allows dice rolls when round is over` () {
            val game = KnisterGame(setOf(Player("toto")))
            game.rollDices()
            game.playerPlacesDicesInSquare(Player("toto"), GridPosition(1, 2))
            game.rollDices()
        }

        @Test
        fun `player cannot place roll if he is not registered for the game` () {
            val game = KnisterGame(setOf(Player("toto")))
            game.rollDices()

            assertThatThrownBy { game.playerPlacesDicesInSquare(Player("titi"), GridPosition(1, 2)) }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `player cannot place the same roll twice` () {
            val player = Player("toto")
            val game = KnisterGame(setOf(player))
            game.rollDices()
            game.playerPlacesDicesInSquare(player, GridPosition(1, 2))

            assertThatThrownBy { game.playerPlacesDicesInSquare(player, GridPosition(2, 3)) }.isInstanceOf(IllegalStateException::class.java)
        }

        @Test
        fun `player cannot place two rolls on the same position` () {
            val player = Player("toto")
            val game = KnisterGame(setOf(player))
            game.rollDices()
            val duplicateGridPosition = GridPosition(1, 2)
            game.playerPlacesDicesInSquare(player, duplicateGridPosition)
            game.rollDices()

            assertThatThrownBy { game.playerPlacesDicesInSquare(player, duplicateGridPosition) }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `game allows 25 dice rolls` () {
            val game = KnisterGame(setOf(Player("toto")))

            for(i in 0  until 25) {
                game.rollDices()
                game.playerPlacesDicesInSquare(Player("toto"), GridPosition(i / 5, i % 5))
            }

            assertThatThrownBy { game.rollDices() }.isInstanceOf(IllegalStateException::class.java)
        }

        @Test
        fun `player can get his score when game is over` () {
            val player = Player("toto")
            val game = KnisterGame(setOf(player))

            for(i in 0  until 25) {
                game.rollDices()
                game.playerPlacesDicesInSquare(player, GridPosition(i / 5, i % 5))
            }

            assertThat(game.score(player)).isPositive()
        }

        @Test
        fun `player cant get his score when game is not over` () {
            val player = Player("toto")
            val game = KnisterGame(setOf(player))

            game.rollDices()

            assertThatThrownBy { game.score(player) }.isInstanceOf(IllegalStateException::class.java)
        }

    }
}
