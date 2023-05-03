fun main() {
    val (a, b) = Array(2) { readln().toBigInteger() }
    print(message = a * b / a.gcd(b))
}
