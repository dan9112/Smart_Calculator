fun main() {
    val text = readln()
    val regexColors = Regex(pattern = "#[\\da-fA-F]{6}\\b")
    regexColors.findAll(text).forEach { println(it.value) }
}
