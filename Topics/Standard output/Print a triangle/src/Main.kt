const val ITERATION_COUNT = 6

fun main() {
    repeat(ITERATION_COUNT) {
        println("#".repeat(it.inc()))
    }
}
