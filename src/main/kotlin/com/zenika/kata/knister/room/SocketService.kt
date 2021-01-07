package com.zenika.kata.knister.room

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class SocketService {
    val sessions = ConcurrentHashMap<String, WebSocketSession>()
    fun registerPlayer(player: String, session: WebSocketSession) {
        sessions[player] = session
    }

    fun unregisterPlayer(session: WebSocketSession) {
        val keys = sessions.filterValues { it == session }.keys
        sessions.remove(keys.firstOrNull())
    }

    fun notifyRooms(roomIds : List<String>) {
        sessions.values.forEach { emit(it, Message("roomList", roomIds)) }
    }

    fun notifyPlayersChange(room: Room) {
        val playerNames = room.players.map { it.name }
        sessionsForRoomPlayers(room).forEach { emit(it, Message("playerList", playerNames)) }
    }

    fun gameStarted(room: Room) {
        sessionsForRoomPlayers(room).forEach { emit(it, Message("gameStarted")) }
    }

    fun diceRolled(room: Room, rollDice: DiceRoll) {
        sessionsForRoomPlayers(room).forEach { emit(it, Message("diceRolled", rollDice)) }
    }

    fun roundCompleted(room: Room) {
        sessionsForRoomPlayers(room).forEach { emit(it, Message("roundCompleted")) }
    }

    fun gameCompleted(room: Room) {
        sessionsForRoomPlayers(room).forEach { emit(it, Message("gameCompleted")) }
    }

    private fun emit(session: WebSocketSession, msg: Message) = session.sendMessage(convertMessage(msg))

    private fun sessionsForRoomPlayers(room: Room): Collection<WebSocketSession> {
        val playerNames = room.players.map { it.name }
        return sessions.filterKeys { playerNames.contains(it) }.values
    }
}
class Message(val msgType: String, val data: Any = "")

fun convertMessage(msg : Message): TextMessage {
    return TextMessage(jacksonObjectMapper().writeValueAsString(msg))
}

