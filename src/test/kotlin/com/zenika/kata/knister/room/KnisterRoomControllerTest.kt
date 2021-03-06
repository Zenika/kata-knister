package com.zenika.kata.knister.room

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.regex.Matcher

// TODO injecter InMemoryRoomRespository pour ne pas dépendre de mongo.
@ExtendWith(SpringExtension::class)
@WebMvcTest(KnisterRoomController::class)
class KnisterRoomControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @SpyBean
    private lateinit var repo : InMemoryRoomRepository

    @MockBean
    private lateinit var socketService: SocketService

    @Test
    fun `given a player he can get the list of room ids`() {
        val room1 = createRoom("Tutu")
        val room2 = createRoom("Tutu")


        mvc.perform(get("/rooms"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$[*]._id", hasItems(room1._id, room2._id)))
    }

    @Test
    fun `given a room is opened it gets an id and a player`() {
        mvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player("Jean-Jacques"))))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$._id", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", hasItems("Jean-Jacques")))

        Mockito.verify(socketService).notifyRooms(Mockito.anyList())
    }

    @Test
    fun `given an open room a player can join`() {
        val room = createRoom("Tutu")

        room.addPlayer("Toto")

        mvc.perform(get("/rooms/${room._id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.players", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", hasItems("Toto", "Tutu")))
    }

    @Test
    fun `given an open room several players can join`() {
        val room = createRoom("Tutu")

        room.addPlayer("Toto").andExpect(status().isOk)
        room.addPlayer("Tata").andExpect(status().isOk)

        mvc.perform(get("/rooms/${room._id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.players", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", hasItems("Toto", "Tata", "Tutu")))
    }

    @Test
    fun `given a room id doesn't exist then getter should return a 404`() {
        mvc.perform(get("/rooms/1234"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `given a room id doesn't exist then adding a player should return a 404`() {
        mvc.perform(post("/rooms/1234/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player("Toto"))))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `trying to add an already existing player should return a 409`() {
        val room = createRoom("Tutu")

        room.addPlayer("Toto").andExpect(status().isOk)
        room.addPlayer("Toto").andExpect(status().isConflict)
    }

    @Test
    fun `Gamemaster can start game`() {
        val room = createRoom("Hugo")

        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.diceRolls", notNullValue()))
    }

    @Test
    fun `when game is started dices can be rolled`() {
        val room = createRoom("Hugo")
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.first", notNullValue()))

    }

    @Test
    fun `when dices are rolled player can place it in the grid`() {
        val playerName = "Hugo"
        val room = createRoom(playerName)
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(GridPosition(0,0))))
                .andExpect(status().isOk)

    }

    @Test
    fun `player cannot place its diceroll twice in the grid`() {
        val playerName = "Hugo"
        val room = createRoom(playerName)
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(GridPosition(0,0))))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(GridPosition(1,1))))
                .andExpect(status().is4xxClientError)

    }

    @Test
    fun `player cannot place two dicerolls on the same position`() {
        val playerName = "Hugo"
        val room = createRoom(playerName)
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(GridPosition(0,0))))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(GridPosition(0,0))))
                .andExpect(status().is4xxClientError)

    }


    @Test
    fun `player can get his score when game is over` () {
        val playerName = "Hugo"
        val room = createRoom(playerName)
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)

        for(i in 0  until 25) {

            mvc.perform(post("/rooms/${room._id}/games/roll"))
                    .andExpect(status().isOk)

            mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(GridPosition(i / 5, i % 5))))
                    .andExpect(status().isOk)
        }
        mvc.perform(get("/rooms/${room._id}/games/scores"))
                .andExpect(status().isOk)


    }

    @Test
    fun `a route is available to get game status`() {
        val playerName = "Hugo"
        val room = createRoom(playerName)
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)

        mvc.perform(get("/rooms/${room._id}/games/status"))
                .andExpect(status().isOk)
    }

    @Test
    fun `player can get his grid info`() {
        val playerName = "Hugo"
        val room = createRoom(playerName)
        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)
        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().isOk)

        mvc.perform(post("/rooms/${room._id}/games/${playerName}/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(GridPosition(0,0))))
                .andExpect(status().isOk)

        mvc.perform(get("/rooms/${room._id}/games/${playerName}/grid"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.lines", notNullValue()))
    }


    @Test
    fun `when a player leaves the room it is no more on the list`() {
        val playerName = "Hugo"
        val leaverName = "Leaver"
        val room = createRoom(playerName)
        room.addPlayer(leaverName)
        mvc.perform(delete("/rooms/${room._id}/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player(leaverName))))
                .andExpect(status().isOk)

        mvc.perform(get("/rooms/${room._id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.players[*].name", hasItems("Hugo")))
    }

    @Test
    fun `when game is not started dices cannot be rolled`() {
        val room = createRoom("Hugo")

        mvc.perform(post("/rooms/${room._id}/games/roll"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun `impossible to start a game if already started`() {
        val room = createRoom("Hugo")

        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.diceRolls", notNullValue()))

        mvc.perform(post("/rooms/${room._id}/games"))
                .andExpect(status().isConflict)
    }

    // TODO only Gamemaster can start a game ?

    private fun createRoom(gameMaster: String): Room {
        val mvcResult = mvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player(gameMaster))))
        .andReturn()
        return mapper.readValue<Room>(mvcResult.response.contentAsString, Room::class.java)
    }

    fun Room.addPlayer(playerName: String): ResultActions {
        return mvc.perform(post("/rooms/${_id}/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player(playerName))))
    }
}
