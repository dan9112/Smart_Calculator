fun getCamelStyleString(inputString: String) = with(StringBuilder()) {
    inputString
        .split('_').forEach {
            it.forEachIndexed { index, c ->
                append(if (index != 0) c else c.uppercase())
            }
        }
    toString()
}
