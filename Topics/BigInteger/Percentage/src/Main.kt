import java.math.BigInteger

private const val HUNDRED = 100L

private val BIG_INTEGER_HUNDRED = BigInteger.valueOf(HUNDRED)

fun main() {
    val (a, b) = Array(2) { readln().toBigInteger() }
    print(
        message = with(receiver = a + b) {
            "${percent(value = a)}% ${percent(value = b)}%"
        }
    )
}

private fun BigInteger.percent(value: BigInteger) = value.multiply(BIG_INTEGER_HUNDRED).divide(this)
