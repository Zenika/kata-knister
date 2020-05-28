package com.zenika.kata.knister.room

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

@RestController
@RequestMapping("/room")
class KnisterRoomController() {
    @PostMapping
    fun openRoom() : Room {
        return Room()
    }
}

data class Room(val id : String = generateRoomId())


fun generateRoomId() : String {
    val alphabet = ('a'..'z')
    return (1 .. 10).map{ alphabet.random() }.joinToString("")

}