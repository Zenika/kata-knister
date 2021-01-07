package com.zenika.kata.knister.room

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.web.socket.WebSocketSession

class SocketServiceTest {
    private lateinit var socketService : SocketService

    @BeforeEach
    fun init() {
        socketService = SocketService()
    }

    @Test
    fun `given a registered player he is notified when a room is created` () {
        val mockSession = mockSessionForPlayer("toto")

        val roomIds = listOf("id1", "id2")
        socketService.notifyRooms(roomIds)

        verify(mockSession).sendMessage(any())
    }


    @Test
    fun `given a registered player he is notified when a room he belongs to starts a game` () {
        val mockSession = mockSessionForPlayer("toto")

        val myRoom = Room()
        myRoom.addPlayer(Player("toto"))

        socketService.gameStarted(myRoom)

        verify(mockSession).sendMessage(any())
    }

    @Test
    fun `given a registered player he isn't notified when a room he doesn't belong to starts a game` () {
        val mockSession = mockSessionForPlayer("toto")

        val myRoom = Room()
        myRoom.addPlayer(Player("titi"))

        socketService.gameStarted(myRoom)

        verify(mockSession, never()).sendMessage(any())
    }

    @Test
    fun `player is notified when dice is rolled` () {
        val mockSession = mockSessionForPlayer("toto")

        val myRoom = Room()
        myRoom.addPlayer(Player("toto"))

        val diceRoll = DiceRoll(Dice(2), Dice(4))
        socketService.diceRolled(myRoom, diceRoll)

        verify(mockSession).sendMessage(convertMessage(Message("diceRolled", diceRoll)))
    }

    private fun mockSessionForPlayer(player: String): WebSocketSession {
        val mockSession = mock(WebSocketSession::class.java)
        socketService.registerPlayer(player, mockSession)
        return mockSession
    }

}