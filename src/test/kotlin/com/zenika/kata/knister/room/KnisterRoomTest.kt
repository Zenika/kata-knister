package com.zenika.kata.knister.room

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*

class KnisterRoomTest {
    @Nested
    inner class RoomIdGeneratorTest {

        @Test
        fun `generated id is not empty`() {
            val id = generateRoomId()

            assertThat(id).isNotEmpty()
        }

        @Test
        fun `generated id is size of 10`() {
            val id = generateRoomId()

            assertThat(id).hasSize(10)
        }

        @Test
        fun `two generated id are differents`() {
            val id1 = generateRoomId()
            val id2 = generateRoomId()

            assertThat(id1).isNotEqualTo(id2)
        }
    }
}