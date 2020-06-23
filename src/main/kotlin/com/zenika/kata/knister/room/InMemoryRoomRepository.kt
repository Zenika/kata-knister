package com.zenika.kata.knister.room

import org.springframework.stereotype.Service

@Service
class InMemoryRoomRepository : RoomRepository { //TODO : trouver une implem qui fait plus BDD

    val rooms = mutableMapOf<String, Room>()

    override fun create(room: Room): Room {
        rooms.put(room.id, room)
        return room
    }

    override fun findOne(id: String): Room? = rooms[id]

    override fun update(room: Room): Room {
        rooms.replace(room.id, room)
        return room
    }
}