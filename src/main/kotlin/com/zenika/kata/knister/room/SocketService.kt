package com.zenika.kata.knister.room

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
@Scope("singleton")
class SocketService {
    private val log = LoggerFactory.getLogger(SocketService::class.java)

    fun registerPlayer(player: String, session: WebSocketSession) {
        log.error("REGISTER PLAYER :$player")
        SessionWrapper.addSession(player, session)
    }

    fun unregisterPlayer(session: WebSocketSession) {
        log.error("UN REGISTER PLAYER")
        SessionWrapper.removePlayerSession(session)
    }

    fun notifyRooms(roomIds : List<String>) {
        SessionWrapper.getAllSessions().forEach { emit(it, Message("roomList", roomIds)) }
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

    private fun emit(session: WebSocketSession, msg: Message) {
        val messageConverted = convertMessage(msg)
        session.sendMessage(messageConverted)
    }

    private fun sessionsForRoomPlayers(room: Room): Collection<WebSocketSession> {
        val playerNames = room.players.map { it.name }
        return SessionWrapper.getPlayersSessions(playerNames)
    }
}
class Message(val msgType: String, val data: Any = "")

fun convertMessage(msg : Message): TextMessage {
    return TextMessage(jacksonObjectMapper().writeValueAsString(msg))
}

object SessionWrapper {
    val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun addSession(playerName : String, session : WebSocketSession) {
        sessions[playerName] = session
    }

    fun removePlayerSession(session : WebSocketSession): WebSocketSession? {
        val keys = sessions.filterValues { it == session }.keys
        return sessions.remove(keys.firstOrNull())
    }

    fun getAllSessions(): MutableCollection<WebSocketSession> {
        return sessions.values
    }

    fun getPlayersSessions(playerNames : Collection<String>): Collection<WebSocketSession> {
        return sessions.filterKeys { playerNames.contains(it) }.values
    }
}

