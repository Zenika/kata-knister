package com.zenika.kata.knister.room

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/rooms")
class KnisterRoomController() {

    // TODO : Use a repository
    val rooms = mutableMapOf<String, Room>()

    @PostMapping
    fun openRoom() : Room {
        val room = Room()
        rooms.put(room.id, room)
        return room
    }

    @GetMapping("/{roomId}")
    fun getRoom(@PathVariable roomId: String): Room {
        return rooms[roomId] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{roomId}/players")
    fun joinRoom(@PathVariable roomId: String, @RequestBody player: Player) {
        rooms[roomId]?.players?.add(player) ?: throw  ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}

data class Room(val id: String = generateRoomId(), val players: MutableSet<Player> = mutableSetOf())

data class Player(val name: String)

fun generateRoomId() : String {
    val alphabet = ('a'..'z')
    return (1 .. 10).map{ alphabet.random() }.joinToString("")

}