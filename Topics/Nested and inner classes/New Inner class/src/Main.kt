data class Vehicle(private val name: String) {
    inner class Body(private val color: String) {
        fun printColor() = println("The $name vehicle has a $color body.")
    }
}
