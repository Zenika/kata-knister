import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KnisterScoreTest {
    @Nested
    inner class LineScoreTest {

        @Test
        fun `no combination scores 0 points`() {
            // Arrange
            var line = Line(intArrayOf(2, 5, 7, 9, 11))

            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(0)
        }

        @Test
        fun `pair scores 1 point`() {
            // Arrange
            val line = Line(intArrayOf(2, 2, 5, 7, 9))
            
            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(1)
        }

        @Test
        fun `three of a kind scores 3 points`() {
            // Arrange
            var line = Line(intArrayOf(2, 2, 2, 7, 9))

            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(3)
        }

        @Test
        fun `four of a kind scores 6 points`() {
            // Arrange
            var line = Line(intArrayOf(2, 2, 2, 2, 9))

            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(6)
        }

        @Test
        fun `five of a kind scores 10 points`() {
            // Arrange
            var line = Line(intArrayOf(2, 2, 2, 2, 2))

            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(10)
        }

        @Test
        fun `two pairs scores 3 points`() {
            // Arrange
            var line = Line(intArrayOf(2, 2, 4, 4, 8))

            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(3)
        }

        @Test
        fun `full house scores 8 points`() {
            // Arrange
            var line = Line(intArrayOf(2, 2, 2, 3, 3))

            // Act
            val result = line.score()

            // Assert
            assertThat(result).isEqualTo(8)
        }

    }

    @Nested
    inner class GridScoreTest {

        @Test
        fun `grid score contains sum of line scores`() {
            // Arrange
            val grid = Grid(arrayOf(
                intArrayOf(2, 2, 2, 2, 5),
                intArrayOf(3, 3, 3, 3, 4),
                intArrayOf(6, 6, 6, 6, 7),
                intArrayOf(8, 8, 8, 8, 9),
                intArrayOf(10, 10, 10, 10, 11)
            ))

            // Act
            val result = grid.score()

            // Assert
            assertThat(result).isEqualTo(30)
        }

        @Test
        fun `grid score contains sum of column scores`() {
            // Arrange
            val grid = Grid(arrayOf(
                intArrayOf(2, 3, 6, 8, 10),
                intArrayOf(2, 3, 6, 8, 10),
                intArrayOf(2, 3, 6, 8, 10),
                intArrayOf(2, 3, 6, 8, 10),
                intArrayOf(5, 4, 7, 9, 11)
            ))

            // Act
            val result = grid.score()

            // Assert
            assertThat(result).isEqualTo(30)
        }

		@Test
        fun `grid score contains sum of diagonal scores with yam and suite that should be equal to 44`() {
            // Arrange
            val grid = Grid(arrayOf(
                intArrayOf( 4,  8,  5,  9,  2),
                intArrayOf(10,  4,  8,  3,  9),
                intArrayOf( 9, 10,  4,  8,  5),
                intArrayOf( 7,  5, 10,  4,  8),
                intArrayOf( 6,  7,  9, 10,  4)
            ))

            // Act
            val result = grid.score()

            // Assert
            assertThat(result).isEqualTo(44)
        }

        @Test
        fun `example grid score is equal to 53`() {
            // Arrange
            val grid = Grid(arrayOf(
                intArrayOf(7,  4,  8, 9, 11),
                intArrayOf(3,  7,  3, 7,  7),
                intArrayOf(5,  5, 10, 5,  5),
                intArrayOf(8,  9,  3, 6,  9),
                intArrayOf(8, 11,  8, 8,  7)
            ))

            // Act
            val result = grid.score()

            // Assert
            assertThat(result).isEqualTo(53)
        }
    }

}
