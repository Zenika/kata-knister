package com.zenika.kata.knister.room

interface RoomRepository {
    fun create(room: Room): Room
    fun findOne(id: String): Room?
    fun findActiveRooms(): List<Room>
    fun update(room: Room): Room
}