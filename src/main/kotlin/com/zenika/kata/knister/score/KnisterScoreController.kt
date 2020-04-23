package com.zenika.kata.knister.score

import Grid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class KnisterScoreController() {

    @PostMapping
    fun gridScore(@RequestBody scoreRequest: ScoreRequest): ScoreResponse {
        return ScoreResponse(scoreRequest.grid.score())
    }
}

data class ScoreRequest(val grid : Grid)
data class ScoreResponse(val score : Int)