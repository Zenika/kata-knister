class Line(val diceRolls : IntArray) {
    fun score() : Int {
        return when (diceRolls.toSet().size) {
            4 -> 1
            3 -> 3
            2 -> fullHouseOrFourOfAKind()
            1 -> 10
            else -> 0
        }
    }

    fun fullHouseOrFourOfAKind(): Int {
        return if (diceRolls.groupBy{it}.mapValues{it.value.size}.values.any{it == 1}) 6 else 8
    }
}