fun main() {
    val inputData = listOf("08:00" to "one billion", "08:30" to "two thousand", "09:10" to "")
    inputData.forEach {
        var output = "${it.first} You have "
        output += if (it.second.isNotEmpty()) it.second + " dollars" else "no dollars!"
        println(output)
    }
}
