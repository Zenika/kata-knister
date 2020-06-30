package com.zenika.kata.knister.room

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

class MongoRoomRepository : RoomRepository {
    private val client = KMongo.createClient()
    private val collection: MongoCollection<Room> = client.getDatabase("knister").getCollection("rooms", Room::class.java)

    override fun create(room: Room): Room {
        collection.save(room)
        return room
    }

    override fun findOne(id: String): Room? {
        return collection.findOneById(id)
    }

    override fun update(room: Room): Room {
        collection.updateOne(room)
        return room
    }
}