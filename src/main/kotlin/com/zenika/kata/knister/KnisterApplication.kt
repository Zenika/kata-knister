package com.zenika.kata.knister

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class KnisterApplication

fun main(args: Array<String>) {
	runApplication<KnisterApplication>(*args)
}
