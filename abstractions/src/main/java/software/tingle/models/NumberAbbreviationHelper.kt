package software.tingle.models

import java.text.DecimalFormat

object NumberAbbreviationHelper {
    private const val FormatTrillions = "0.###T"
    private const val FormatBillions = "0.###B"
    private const val FormatMillions = "0.##M"
    private const val FormatKilo = "0.#k"

    @JvmStatic
    fun abbreviate(source: Long): String {
        return if (source > 999_999_999_999L || source < -999_999_999_999L) {
            val format = DecimalFormat(FormatTrillions)
            format.format((source / 1000000000000L.toFloat()).toDouble())
        } else if (source > 999_999_999L || source < -999_999_999L) {
            val format = DecimalFormat(FormatBillions)
            format.format((source / 1000000000L.toFloat()).toDouble())
        } else if (source > 999_999L || source < -999_999L) {
            val format = DecimalFormat(FormatMillions)
            format.format((source / 1000000L.toFloat()).toDouble())
        } else if (source > 999L || source < -999L) {
            val format = DecimalFormat(FormatKilo)
            format.format((source / 1_000L.toFloat()).toDouble())
        } else "" + source
    }
}