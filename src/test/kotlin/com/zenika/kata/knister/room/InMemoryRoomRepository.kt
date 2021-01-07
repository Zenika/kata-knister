package com.zenika.kata.knister.room

import org.springframework.stereotype.Service

@Service
class InMemoryRoomRepository : RoomRepository {
    val rooms = mutableMapOf<String, Room>()

    override fun create(room: Room): Room {
        rooms.put(room._id, room)
        return room
    }

    override fun findOne(id: String): Room? = rooms[id]
    override fun findActiveRooms(): List<Room> {
        return rooms.values.filter{ it.players.isNotEmpty() }.toList()
    }

    override fun update(room: Room): Room {
        rooms.replace(room._id, room)
        return room
    }
}