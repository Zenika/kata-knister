package com.zenika.kata.knister

import com.fasterxml.jackson.databind.ObjectMapper
import com.zenika.kata.knister.room.SocketService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
    fun getSocketService(): SocketService = SocketService()

    @Bean
    fun productApi(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.zenika.kata.knister.room"))
            .build()

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(KnisterSocketHandler(getSocketService()), "/knisterSocket")
                .setAllowedOrigins("*")

    }
}

class Message(val msgType: String, val data: Any)

class KnisterSocketHandler(val socketService: SocketService) : TextWebSocketHandler() {

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        socketService.unregisterPlayer(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = ObjectMapper().readTree(message.payload)
        val player = json.get("playerName").asText()
        socketService.registerPlayer(player, session)
    }

}
