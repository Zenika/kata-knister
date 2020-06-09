package com.zenika.kata.knister.room

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@ExtendWith(SpringExtension::class)
@WebMvcTest(KnisterRoomController::class)
class KnisterRoomControllerTest() {
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun `given a room is opened it gets an id`() {
        mvc.perform(post("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
    }

    @Test
    fun `given an open room a player can join`() {
        var room = createRoom()

        room.addPlayer("Toto")

        mvc.perform(get("/rooms/${room.id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", contains("Toto")))
    }

    @Test
    fun `given an open room several players can join`() {
        var room = createRoom()

        room.addPlayer("Toto").andExpect(status().isOk())
        room.addPlayer("Tata").andExpect(status().isOk())

        mvc.perform(get("/rooms/${room.id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", notNullValue()))
                .andExpect(jsonPath("$.players[*].name", contains("Toto", "Tata")))
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
        var room = createRoom();

        room.addPlayer("Toto").andExpect(status().isOk())
        room.addPlayer("Toto").andExpect(status().isConflict())
    }

    private fun createRoom(): Room {
        val mvcResult = mvc.perform(post("/rooms")).andReturn();
        return mapper.readValue<Room>(mvcResult.response.contentAsString, Room::class.java)
    }

    fun Room.addPlayer(playerName: String): ResultActions {
        return mvc.perform(post("/rooms/${id}/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Player(playerName))))
    }
}
