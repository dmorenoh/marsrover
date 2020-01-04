package marsrover.domain.value

fun Coordinate.plus(coordinate: Coordinate): Coordinate =
        Coordinate(this.xPoint + coordinate.xPoint, this.yPoint + coordinate.yPoint)
fun Coordinate.minus(coordinate: Coordinate):
        Coordinate = Coordinate(this.xPoint - coordinate.xPoint, this.yPoint - coordinate.yPoint)
fun Coordinate.asPositionInMapSize(map: MapSize): Coordinate = map.getPosition(this)

fun MapSize.getPosition(coordinate: Coordinate) = Coordinate(
        getValidPosition(coordinate.xPoint, this.sizeX),
        getValidPosition(coordinate.yPoint, this.sizeY))


private fun getValidPosition(nextPosition: Int, max: Int): Int {
    return when {
        nextPosition > max -> 0
        nextPosition < 0 -> max
        else -> nextPosition
    }
}