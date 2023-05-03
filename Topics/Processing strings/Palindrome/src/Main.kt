fun main() {
    val input = readln()
    for (index in 0..input.length / 2) {
        if (input[index] != input[input.lastIndex - index]) {
            println("no")
            return
        }
    }
    println("yes")
}
