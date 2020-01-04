package marsrover.domain.model

import marsrover.domain.MarsRoverException
import marsrover.domain.value.Coordinate
import marsrover.domain.value.MapSize
import marsrover.domain.value.asPositionInMapSize

data class MarsRoverMap(var mapSize: MapSize,
                        var obstacles: List<Coordinate>) {

    fun getAsValidPosition(coordinate: Coordinate): Coordinate {
        val finalPosition = coordinate.asPositionInMapSize(this.mapSize)
        if (obstacles.contains(finalPosition)) {
            throw MarsRoverException("Obstacle found in next position")
        }
        return finalPosition
    }
}