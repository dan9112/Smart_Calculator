fun summator(map: Map<Int, Int>) = run {
    var sum = 0
    map.forEach { (key, value) -> if (key % 2 == 0) sum += value }
    sum
}
