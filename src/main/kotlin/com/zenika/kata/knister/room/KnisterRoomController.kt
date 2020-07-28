package com.zenika.kata.knister.room

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.lang.IllegalStateException
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/rooms")
class KnisterRoomController(@Autowired val roomRepository: RoomRepository) {

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

    @PostMapping("/{roomId}/games")
    fun startGame(@PathVariable roomId: String): KnisterGame {
        var room = getRoom(roomId)
        val game = room.startGame()
        roomRepository.update(room)
        return game
    }

    // TODO : un jour nous lancerons des dés
    @PostMapping("/{roomId}/games/roll")
    fun rollDices(@PathVariable roomId: String) : KnisterGame {
        var room = getRoom(roomId)
        val game = getRoom(roomId).currentGame()
        game.rollDices()
        roomRepository.update(room)
        return game
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(GameAlreadyStartedException::class)
    fun handleBadRequest(req: HttpServletRequest, ex: Exception?) {}

    // TODO : avoir un seul handler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PlayerAlreadExistsException::class)
    fun handleGameBadRequest(req: HttpServletRequest, ex: Exception?) {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalStateException::class)
    fun handlerRequiresRequest(req: HttpServletRequest, ex: Exception?) {}

}

