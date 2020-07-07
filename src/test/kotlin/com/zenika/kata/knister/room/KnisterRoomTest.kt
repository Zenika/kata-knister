package com.zenika.kata.knister.room

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KnisterRoomTest {
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
}