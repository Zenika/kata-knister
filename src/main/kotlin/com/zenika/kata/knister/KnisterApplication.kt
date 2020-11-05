package com.zenika.kata.knister

import com.zenika.kata.knister.room.MongoRoomRepository
import com.zenika.kata.knister.room.RoomRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
class KnisterConfiguration {
	@Bean
	fun getRoomRepository() : RoomRepository = MongoRoomRepository()

	@Bean
	fun productApi() : Docket  = Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.zenika.kata.knister.room")).build()
}