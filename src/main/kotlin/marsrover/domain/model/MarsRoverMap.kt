package marsrover.domain.model

import marsrover.domain.exception.MarsRoverMapException

data class MarsRoverMap(private var area: Area,
                        private var obstacles: List<Coordinate>) {

    fun calculateCoordinate(coordinate: Coordinate): Coordinate {
        val calculatedPosition = area.calculatePosition(coordinate)
        if (obstacles.contains(calculatedPosition)) {
            throw MarsRoverMapException("Obstacle found in next position")
        }
        return calculatedPosition
    }
}