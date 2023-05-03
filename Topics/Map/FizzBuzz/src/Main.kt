private const val THREE = 3
private const val FIVE = 5

fun iterator(map: Map<String, Int>) = map.forEach { (_, u) ->
    println(
        when {
            u % THREE == 0 -> "Fizz"
            u % FIVE == 0 -> "Buzz"
            else -> "FizzBuzz"
        }
    )
}
