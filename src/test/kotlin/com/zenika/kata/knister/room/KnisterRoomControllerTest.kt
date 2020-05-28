package com.zenika.kata.knister.room

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@ExtendWith(SpringExtension::class)
@WebMvcTest(KnisterRoomController::class)
class KnisterRoomControllerTest () {
    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `given true is true`() {
        mvc.perform(post("/room"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
    }
}