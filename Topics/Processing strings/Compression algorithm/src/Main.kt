fun main() = println(
    with(StringBuilder()) {
        var count = 1
        readln().forEach {
            if (isNotEmpty() && last() == it) {
                count++
            } else {
                if (isNotEmpty()) append(count)
                append(it)
                count = 1
            }
        }
        append(count)
    }
)
