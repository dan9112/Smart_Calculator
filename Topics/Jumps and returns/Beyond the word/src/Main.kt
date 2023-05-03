fun main() {
    val input = readln()
    var char = 'a'
    while (char <= 'z') {
        if (input.contains(char)) char++
        else print(char++)
    }
}