package com.zenika.kata.knister

import com.zenika.kata.knister.room.MongoRoomRepository
import com.zenika.kata.knister.room.RoomRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
open class KnisterApplication

fun main(args: Array<String>) {
	runApplication<KnisterApplication>(*args)
}

@Configuration
class KnisterConfiguration {
	@Bean
	fun getRoomRepository() : RoomRepository = MongoRoomRepository()
}