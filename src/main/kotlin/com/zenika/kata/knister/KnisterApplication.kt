package com.zenika.kata.knister

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zenika.kata.knister.room.MongoRoomRepository
import com.zenika.kata.knister.room.Player
import com.zenika.kata.knister.room.Room
import com.zenika.kata.knister.room.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication(exclude = arrayOf(MongoAutoConfiguration::class))
@EnableSwagger2
open class KnisterApplication

fun main(args: Array<String>) {
    runApplication<KnisterApplication>(*args)
}

@Configuration
@EnableWebSocket
class WSConfig : WebSocketConfigurer {
    @Bean
    fun getRoomRepository(): RoomRepository = MongoRoomRepository()

    @Bean
    fun productApi(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.zenika.kata.knister.room"))
            .build()

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(RoomHandler(getRoomRepository()), "/roomSocket")
                .setAllowedOrigins("*")

    }
}

class Message(val msgType: String, val data: Any)

class RoomHandler(val roomRepository: RoomRepository) : TextWebSocketHandler() {
    val roomSessions = HashMap<String, RoomSessions>()


    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        // TODO : informer les autres joueurs qu'il a quitté la salle ?
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = ObjectMapper().readTree(message.payload)
        val roomId = json.get("room").asText()
        val player = json.get("playerName").asText()
        val room = roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        when (json.get("type").asText()) {
            "created" -> {
                val roomSession = RoomSessions(roomId)
                roomSession.joinRoom(session, room, player)
                roomSessions[room._id] = roomSession
            }
            "quit" -> {
                val roomSession = roomSessions[roomId]
                roomSession?.quitRoom(room, player)
            }
            "joined" -> {
                val roomSession = roomSessions[roomId]
                roomSession?.joinRoom(session, room, player)
            }
            "updated" -> { //démarrage de partie, après lancer de dé, après positionnement d'un lancer dans la grille
                val roomSession = roomSessions[roomId]
                val room = roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
                roomSession?.updateStatus(room)
            }

        }
    }

}

class RoomSessions(val roomId: String) {
    val sessionList = HashMap<String, WebSocketSession>()
    fun joinRoom(session: WebSocketSession, room : Room, playerName: String) {
        sessionList[playerName] = session
        updatePlayers(room)
    }

    fun quitRoom(room : Room, playerName: String) {
        sessionList.remove(playerName)
        updatePlayers(room)
    }

    fun updateStatus(room : Room) {
        sessionList.forEach { emit(it.value, Message("updateStatus", room.currentGame().toGameStatus())) }
    }

    private fun updatePlayers(room : Room) {
        sessionList.forEach { emit(it.value, Message("updatePlayers", room.players)) }
    }

    fun emit(session: WebSocketSession, msg: Message) = session.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(msg)))

}
