fun findSerialNumberForGame(listGames: List<String>) {
    val desired = Regex(pattern = "\\b${readln()}@\\d+@\\d+")
    println(
        message = listGames
            .find { it.matches(regex = desired) }
            ?.split('@')
            ?.let { "Code for Xbox - ${it[1]}, for PC - ${it[2]}" }
            ?: "No such games"
    )
}
