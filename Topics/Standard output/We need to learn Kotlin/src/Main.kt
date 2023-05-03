const val DELIMITER = "\n"

fun main() {
    "We need\nto learn kotlin\nas quickly as possible"
        .uppercase()
        .split(Regex("(?=$DELIMITER)"))
        .forEach {
            print(it.replace(DELIMITER, DELIMITER + DELIMITER))
        }
}
