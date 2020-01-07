package marsrover.domain.model

import arrow.core.Option
import marsrover.domain.exception.MarsRoverException

enum class DirectionType(private val value: String,
                         private val addressedCoordinate: Coordinate) {
    EAST("e", Coordinate(1, 0)) {
        override fun left() = NORTH
        override fun right() = SOUTH
    },
    NORTH("n", Coordinate(0, 1)) {
        override fun left() = WEST
        override fun right() = EAST
    },
    WEST("w", Coordinate(-1, 0)) {
        override fun left() = SOUTH
        override fun right() = NORTH
    },
    SOUTH("s", Coordinate(0, -1)) {
        override fun left() = EAST
        override fun right() = WEST
    };

    abstract fun left(): DirectionType

    abstract fun right(): DirectionType

    open fun forwardFrom(currentPosition: Coordinate): Coordinate {
        return currentPosition + (addressedCoordinate)
    }

    open fun backwardFrom(currentPosition: Coordinate): Coordinate {
        return currentPosition - (addressedCoordinate)
    }

    override fun toString(): String {
        return value
    }

    companion object {
        @JvmStatic
        fun getEnum(value: String): DirectionType {
            return Option.fromNullable(values()
                    .firstOrNull { it.value == value })
                    .fold({ throw MarsRoverException("Invalid direction!") }, { it })
        }
    }

}
