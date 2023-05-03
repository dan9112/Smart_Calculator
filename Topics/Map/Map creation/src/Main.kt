fun stringToMap(first: String, second: String, third: String): Map<String, Int> = mapOf(
    first.convertToPairWithItsLength, second.convertToPairWithItsLength, third.convertToPairWithItsLength
)

private val String.convertToPairWithItsLength
    get() = this to length