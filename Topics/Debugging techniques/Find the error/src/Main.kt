import java.util.*

val IntArray.swapInts
    get() = this.also { array ->
        array[0] += array[1]
        array[1] = array[0] - array[1]
        array[0] -= array[1]
    }

fun main() = with(
    Scanner(System.`in`)
) {
    while (hasNextLine()) {
        val ints = intArrayOf(
            nextLine().toInt(),
            nextLine().toInt()
        )
        val newInts = ints.clone().swapInts
        check(newInts[0] == ints[1] && newInts[1] == ints[0]) { "Numbers haven't swapped!" }
        println(newInts[0])
        println(newInts[1])
    }
}
