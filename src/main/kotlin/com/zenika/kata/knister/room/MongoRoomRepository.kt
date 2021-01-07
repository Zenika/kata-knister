package com.zenika.kata.knister.room

import org.litote.kmongo.KMongo
import org.litote.kmongo.findOneById
import org.litote.kmongo.save
import org.litote.kmongo.updateOne


class MongoRoomRepository : RoomRepository {
    private val mongoConnectionString = System.getenv("MONGOCSTRING")
    private val roomCollection = KMongo.createClient(mongoConnectionString)
                                                                .getDatabase("knister")
                                                                .getCollection("rooms", Room::class.java)

    override fun create(room: Room): Room {
        roomCollection.save(room)
        return room
    }

    override fun findOne(id: String): Room? {
        return roomCollection.findOneById(id)
    }

    override fun findActiveRooms(): List<Room> {
        return roomCollection.find().filter{ it.players.isNotEmpty() }.toList()
    }

    override fun update(room: Room): Room {
        roomCollection.updateOne(room)
        return room
    }

}