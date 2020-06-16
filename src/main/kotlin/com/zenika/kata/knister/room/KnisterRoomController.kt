package com.zenika.kata.knister.room

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/rooms")
class KnisterRoomController(@Autowired val roomRepository: RoomRepository = InMemoryRoomRepository()) {

    @PostMapping
    fun openRoom(@RequestBody player: Player): Room {
        val room = Room()
        room.addPlayer(player)
        return roomRepository.create(room)
    }

    @GetMapping("/{roomId}")
    fun getRoom(@PathVariable roomId: String): Room {
        return roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{roomId}/players")
    fun joinRoom(@PathVariable roomId: String, @RequestBody player: Player) {
        var room = getRoom(roomId)
        room.addPlayer(player)
        roomRepository.update(room)
    }

}

data class Room(val id: String = generateRoomId(), val players: MutableSet<Player> = mutableSetOf()) {
    fun addPlayer(player: Player) {
        if (players.contains(player))
            throw ResponseStatusException(HttpStatus.CONFLICT)

        players.add(player)
    }
}

data class Player(val name: String)

fun generateRoomId(): String {
    val alphabet = ('a'..'z')
    return (1..10).map { alphabet.random() }.joinToString("")
}