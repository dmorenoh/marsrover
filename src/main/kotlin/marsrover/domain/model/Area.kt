package marsrover.domain.model

data class Area(private val sizeX: Size,
                private val sizeY: Size) {
    fun calculatePosition(coordinate: Coordinate): Coordinate {
        val posX = sizeX.calculatePos(coordinate.xPoint)
        val posY = sizeY.calculatePos(coordinate.yPoint)
        return Coordinate(posX, posY)
    }
}