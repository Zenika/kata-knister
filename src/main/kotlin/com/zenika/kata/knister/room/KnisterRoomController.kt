package com.zenika.kata.knister.room

import Grid
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
        val room = getRoom(roomId)
        room.addPlayer(player)
        roomRepository.update(room)
    }

    @DeleteMapping("/{roomId}/players")
    fun leaveRoom(@PathVariable roomId: String, @RequestBody player: Player) {
        val room = getRoom(roomId)
        room.removePlayer(player)
        roomRepository.update(room)
    }

    @PostMapping("/{roomId}/games")
    fun startGame(@PathVariable roomId: String): KnisterGame {
        val room = getRoom(roomId)
        val game = room.startGame()
        roomRepository.update(room)
        return game
    }

    @PostMapping("/{roomId}/games/roll")
    fun rollDices(@PathVariable roomId: String) : DiceRoll {
        val room = getRoom(roomId)
        val game = room.currentGame()
        val rollDices = game.rollDices()
        roomRepository.update(room)
        return rollDices
    }

    @PostMapping("/{roomId}/games/{player}/grid")
    fun placeDiceInGrid(@PathVariable roomId: String, @PathVariable player: String, @RequestBody gridPosition: GridPosition) {
        val room = getRoom(roomId)
        val game = room.currentGame()
        game.playerPlacesDicesInSquare(Player(player), gridPosition)
        roomRepository.update(room)
    }

    @GetMapping("/{roomId}/games/{player}/grid")
    fun getGrid(@PathVariable roomId: String, @PathVariable player: String): Grid {
        val room = roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return room.currentGame().gridsForPlayers[Player(player)] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [GameAlreadyStartedException::class,
                                PlayerAlreadExistsException::class,
                                IllegalArgumentException::class,
                                IllegalStateException::class])
    fun handlerCheckRequest(req: HttpServletRequest, ex: Exception?) {}

}

