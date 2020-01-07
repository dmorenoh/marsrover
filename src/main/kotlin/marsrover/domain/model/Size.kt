package marsrover.domain.model

data class Size(private val value: Int) {
    fun calculatePos(position: Int): Int = when {
        position > value -> 0
        position < 0 -> value
        else -> position
    }
}