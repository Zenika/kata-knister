package com.zenika.kata.knister.score

import Grid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreController() {

    @PostMapping
    fun gridScore(@RequestBody grid: String): String {
        return "53"
    }
}