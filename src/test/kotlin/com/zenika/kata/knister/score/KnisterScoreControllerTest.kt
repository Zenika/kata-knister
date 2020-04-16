package com.zenika.kata.knister.score

import org.hamcrest.Matchers.`is`
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
@WebMvcTest(KnisterScoreController::class)
class KnisterScoreControllerTest () {
    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `given a grid the controller compute the score`() {
        var grid = """{
            "grid": [ 
                [7,  4,  8, 9, 11],
                [3,  7,  3, 7,  7],
                [5,  5, 10, 5,  5],
                [8,  9,  3, 6,  9],
                [8, 11,  8, 8,  7] 
            ]
        }"""

        mvc.perform(post("/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(grid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.score", `is`(53)))
    }
}