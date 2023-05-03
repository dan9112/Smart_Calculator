fun solution(names: List<String>): Int {
    for (index in 1..names.lastIndex step 2) {
        if (names[index].startsWith('T')) return index
    }
    return -1
}