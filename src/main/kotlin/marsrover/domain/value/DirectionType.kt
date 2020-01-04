package marsrover.domain.value

enum class DirectionType(val value: String,
                         private val directionCoordinate: Coordinate) {
    EAST("e", Coordinate(1, 0)) {
        override fun turnLeft() = NORTH
        override fun turnRight() = SOUTH
    },
    NORTH("n", Coordinate(0, 1)) {
        override fun turnLeft() = WEST
        override fun turnRight() = EAST
    },
    WEST("w", Coordinate(-1, 0)) {
        override fun turnLeft() = SOUTH
        override fun turnRight() = NORTH
    },
    SOUTH("s", Coordinate(0, -1)) {
        override fun turnLeft() = EAST
        override fun turnRight() = WEST
    };

    abstract fun turnLeft(): DirectionType

    abstract fun turnRight(): DirectionType

    open fun moveForwardFrom(currentPosition: Coordinate): Coordinate {
        return currentPosition.plus(directionCoordinate)
    }

    open fun moveBackwardFrom(currentPosition: Coordinate): Coordinate {
        return currentPosition.minus(directionCoordinate)
    }

    override fun toString(): String {
        return value
    }

    companion object {
        @JvmStatic
        fun getEnum(value: String): DirectionType {
            for (v in values()) if (v.value == value) return v
            throw IllegalArgumentException()
        }
    }

}
