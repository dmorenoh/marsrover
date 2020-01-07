package marsrover.domain.model

data class MarsRover(private var currentDirection: DirectionType,
                     private var currentPosition: Coordinate,
                     private val map: MarsRoverMap) {
    fun turnLeft() {
        currentDirection = currentDirection.left()
    }

    fun turnRight() {
        currentDirection = currentDirection.right()
    }

    fun moveForward() {
        val nextPosition = currentDirection.forwardFrom(currentPosition)
        currentPosition = map.calculateCoordinate(nextPosition)
    }

    fun moveBackward() {
        val nextPosition = currentDirection.backwardFrom(currentPosition)
        currentPosition = map.calculateCoordinate(nextPosition)
    }

    fun currentPositionAsString(): String = "X:${currentPosition.xPoint}, Y:${currentPosition.yPoint}"
    fun currentDirectionAsString(): String = "facing:${currentDirection.value}"
}