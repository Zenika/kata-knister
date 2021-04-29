package com.zenika.kata.knister.room

import com.mongodb.client.model.UpdateOptions
import org.litote.kmongo.KMongo
import org.litote.kmongo.findOneById
import org.litote.kmongo.save
import org.litote.kmongo.updateOne


class MongoRoomRepository : RoomRepository {
    private val mongoConnectionString = System.getenv("MONGOCSTRING")
    private val roomCollection = KMongo.createClient(mongoConnectionString)
                                                                .getDatabase("knister")
                                                                .getCollection("rooms", Room::class.java)
    private val updateOptions = UpdateOptions().upsert(false)

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
        val currentVersion = room.version
        room.version++
        val result = roomCollection.updateOne("{_id=${room._id},version=$currentVersion}", room, updateOptions)
        if (result.modifiedCount == 0L) throw ConcurrentAccessException("Room modified by another user")
        return room
    }

}