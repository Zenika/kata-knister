package com.zenika.kata.knister.room

class PlayerAlreadExistsException(message : String) : Exception(message)
class GameAlreadyStartedException(message : String) : Exception(message)
class ConcurrentAccessException(message : String) : Exception(message)