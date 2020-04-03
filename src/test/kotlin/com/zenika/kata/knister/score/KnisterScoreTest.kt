import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*

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
}
