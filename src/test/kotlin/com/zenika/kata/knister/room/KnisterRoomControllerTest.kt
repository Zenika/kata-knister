package com.zenika.kata.knister.room

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.beans.BeanProperty


@ExtendWith(SpringExtension::class)
@WebMvcTest(KnisterRoomController::class)
class KnisterRoomControllerTest() {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun `given a room is opened it gets an id and a player`() {
        mvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player("Jean-Jacques"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", containsInAnyOrder("Jean-Jacques")))
    }

    @Test
    fun `given an open room a player can join`() {
        var room = createRoom("Tutu")

        room.addPlayer("Toto")

        mvc.perform(get("/rooms/${room.id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", containsInAnyOrder("Toto", "Tutu")))
    }

    @Test
    fun `given an open room several players can join`() {
        var room = createRoom("Tutu")

        room.addPlayer("Toto").andExpect(status().isOk())
        room.addPlayer("Tata").andExpect(status().isOk())

        mvc.perform(get("/rooms/${room.id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", containsInAnyOrder("Toto", "Tata", "Tutu")))
    }

    @Test
    fun `given a room id doesn't exist then getter should return a 404`() {
        mvc.perform(get("/rooms/1234"))
                .andExpect(status().isNotFound())
    }

    @Test
    fun `given a room id doesn't exist then adding a player should return a 404`() {
        mvc.perform(post("/rooms/1234/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player("Toto"))))
                .andExpect(status().isNotFound())
    }

    @Test
    fun `trying to add an already existing player should return a 409`() {
        var room = createRoom("Tutu");

        room.addPlayer("Toto").andExpect(status().isOk())
        room.addPlayer("Toto").andExpect(status().isConflict())
    }

    private fun createRoom(gameMaster: String): Room {
        val mvcResult = mvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player(gameMaster))))
        .andReturn();
        return mapper.readValue<Room>(mvcResult.response.contentAsString, Room::class.java)
    }

    fun Room.addPlayer(playerName: String): ResultActions {
        return mvc.perform(post("/rooms/${id}/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player(playerName))))
    }
}
