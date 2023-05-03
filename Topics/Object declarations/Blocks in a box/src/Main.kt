import kotlin.properties.Delegates.notNull

data class Block(val color: String) {
    object BlockProperties {
        var length by notNull<Int>()
        var width by notNull<Int>()

        fun blocksInBox(length: Int, width: Int) = length / this.length * (width / this.width)
    }
}
