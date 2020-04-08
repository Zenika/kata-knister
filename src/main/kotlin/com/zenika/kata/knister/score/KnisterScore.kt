enum class Figure(val combination: IntArray, val score: Int) {
    PAIR(intArrayOf(2, 1, 1, 1), 1),
    DOUBLE_PAIR(intArrayOf(2, 2, 1), 3),
    THREE_OF_A_KIND(intArrayOf(3, 1, 1), 3),
    FOUR_OF_A_KIND(intArrayOf(4, 1), 6),
    FULL_HOUSE(intArrayOf(3, 2), 8),
    YAMS(intArrayOf(5), 10)

}

fun findCorrespondingFigure(dices: IntArray): Figure? {
    return Figure.values().find { it.combination contentEquals dices }
}

class Line(private val diceResults: IntArray) {
    private val COMMON_STRAIGHT_DICE_ROLL = 7

    fun score(): Int {
        val figureCombination = countDiceRollsGroupedByFaces()
        val figure = findCorrespondingFigure(figureCombination)
        return figure?.score ?: nonFigureScore()
    }

    private fun countDiceRollsGroupedByFaces(): IntArray {
        val scoresByValue = diceResults.groupBy { it }.mapValues { it.value.size }
        return scoresByValue.values.sortedDescending().toIntArray()
    }

    private fun nonFigureScore(): Int {
        return if (isStraight())
            straightScore()
        else 0
    }

    private fun straightScore(): Int {
        return if (diceResults.contains(COMMON_STRAIGHT_DICE_ROLL))
            8
        else 12
    }

    private fun isStraight() = (diceResults.max()!! - diceResults.min()!! == 4)
}
