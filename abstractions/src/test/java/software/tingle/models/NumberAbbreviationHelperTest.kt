package software.tingle.models

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class NumberAbbreviationHelperTest(private val original: Long, private val expected: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>> {
            return listOf(
                    arrayOf(1_001, "1k"),
                    arrayOf(1_010, "1k"),
                    arrayOf(10_300, "10.3k"),
                    arrayOf(3_900_120, "3.9M"),
                    arrayOf(3_910_120, "3.91M"),
                    arrayOf(3_000_120, "3M"),
                    arrayOf(1_400_000_120, "1.4B"),
                    arrayOf(1_000_000_120, "1B"),
                    arrayOf(1_004_000_120, "1.004B"),
                    arrayOf(1_044_000_120, "1.044B"),
                    arrayOf(10_044_000_120, "10.044B"),
                    arrayOf(10_044_000_120_000, "10.044T")
            )
        }
    }

    @Test
    fun abbreviateWorks() {
        val actual = NumberAbbreviationHelper.abbreviate(original)
        Assert.assertEquals(expected, actual)
    }
}