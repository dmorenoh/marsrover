package marsrover.domain.model

import marsrover.domain.value.Coordinate
import marsrover.domain.value.DirectionType

data class MarsRover(var currentDirection: DirectionType,
                     var currentPosition: Coordinate,
                     val map: MarsRoverMap) {
    fun turnLeft() {
        currentDirection = currentDirection.turnLeft()
    }

    fun turnRight() {
        currentDirection = currentDirection.turnRight()
    }

    fun moveForward() {
        val nextPosition = currentDirection.moveForwardFrom(currentPosition)
        currentPosition = map.getAsValidPosition(nextPosition)
    }

    fun moveBackward() {
        val nextPosition = currentDirection.moveBackwardFrom(currentPosition)
        currentPosition = map.getAsValidPosition(nextPosition)
    }
}