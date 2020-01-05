package marsrover.domain.value

import marsrover.domain.MarsRoverException
import marsrover.domain.model.MarsRoverMap

fun Coordinate.plus(coordinate: Coordinate): Coordinate =
        Coordinate(this.xPoint + coordinate.xPoint,
                this.yPoint + coordinate.yPoint)

fun Coordinate.minus(coordinate: Coordinate):
        Coordinate = Coordinate(this.xPoint - coordinate.xPoint,
        this.yPoint - coordinate.yPoint)

fun Coordinate.asMapCoordinateOf(map: MarsRoverMap): Coordinate {
    val position = this.asPositionInMapSize(map.mapSize)
    if (map.obstacles.contains(position)) {
        throw MarsRoverException("Obstacle found in next position")
    }
    return position
}

fun Coordinate.asPositionInMapSize(map: MapSize): Coordinate = Coordinate(
        getValidPosition(this.xPoint, map.sizeX),
        getValidPosition(this.yPoint, map.sizeY))

private fun getValidPosition(nextPosition: Int, max: Int): Int {
    return when {
        nextPosition > max -> 0
        nextPosition < 0 -> max
        else -> nextPosition
    }
}