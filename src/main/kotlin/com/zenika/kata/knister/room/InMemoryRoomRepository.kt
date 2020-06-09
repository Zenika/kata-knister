package com.zenika.kata.knister.room

import org.springframework.stereotype.Service

@Service
class InMemoryRoomRepository : RoomRepository {

    val rooms = mutableMapOf<String, Room>()

    override fun create(room: Room): Room {
        rooms.put(room.id, room)
        return room.copy()
    }

    override fun findOne(id: String): Room? = rooms[id]?.copy()

    override fun update(room: Room): Room {
        rooms.replace(room.id, room)
        return room.copy()
    }
}