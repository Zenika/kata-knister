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
@CrossOrigin(origins = ["http://localhost:3000"])
class KnisterRoomController(@Autowired val roomRepository: RoomRepository, @Autowired val socketService: SocketService) {

    @PostMapping
    fun openRoom(@RequestBody player: Player): Room {
        val room = Room()
        room.addPlayer(player)
        val createdRoom = roomRepository.create(room)
        val roomIds = roomRepository.findActiveRooms().map { it -> it._id }
        socketService.notifyRooms(roomIds)
        return createdRoom
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
        socketService.notifyPlayersChange(room)
    }

    @DeleteMapping("/{roomId}/players")
    fun leaveRoom(@PathVariable roomId: String, @RequestBody player: Player) {
        val room = getRoom(roomId)
        room.removePlayer(player)
        roomRepository.update(room)
        socketService.notifyPlayersChange(room)
    }

    @PostMapping("/{roomId}/games")
    fun startGame(@PathVariable roomId: String): KnisterGame {
        val room = getRoom(roomId)
        val game = room.startGame()
        roomRepository.update(room)
        socketService.gameStarted(room)
        return game
    }

    @PostMapping("/{roomId}/games/roll")
    fun rollDices(@PathVariable roomId: String) : DiceRoll {
        val room = getRoom(roomId)
        val game = room.currentGame()
        val rollDices = game.rollDices()
        roomRepository.update(room)
        socketService.diceRolled(room, rollDices)
        return rollDices
    }

    @PostMapping("/{roomId}/games/{player}/grid")
    fun placeDiceInGrid(@PathVariable roomId: String, @PathVariable player: String, @RequestBody gridPosition: GridPosition) {
        val room = getRoom(roomId)
        val game = room.currentGame()
        game.playerPlacesDicesInSquare(Player(player), gridPosition)
        if(room.roundOver()) {
            socketService.roundCompleted(room)
        }
        if(room.gameOver()) {
            socketService.gameCompleted(room)
        }
        roomRepository.update(room)
    }

    @GetMapping("/{roomId}/games/{player}/grid")
    fun getGrid(@PathVariable roomId: String, @PathVariable player: String): Grid {
        val room = roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return room.currentGame().gridsForPlayers[Player(player)] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    }

    @GetMapping("/{roomId}/games/scores")
    fun getScores(@PathVariable roomId: String): Map<Player, Int> {
        val room = roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return room.currentGame().scores()
    }

    @GetMapping("/{roomId}/games/status")
    fun getGameStatus(@PathVariable roomId: String): GameStatus {
        val room = roomRepository.findOne(roomId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return room.currentGame().toGameStatus()
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [
        GameAlreadyStartedException::class,
                                PlayerAlreadExistsException::class
    ])
    fun handlerConflictRequest(req: HttpServletRequest, ex: Exception?) {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [
        IllegalArgumentException::class,
        IllegalStateException::class
    ])
    fun handlerBadRequestRequest(req: HttpServletRequest, ex: Exception?) {}

}

