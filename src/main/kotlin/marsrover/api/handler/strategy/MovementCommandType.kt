package marsrover.api.handler.strategy

import arrow.core.Option
import arrow.core.rightIfNull
import marsrover.domain.MarsRoverException
import marsrover.domain.model.MarsRover
import kotlin.reflect.KFunction1

enum class MovementCommandType(val value: String,
                               private val function: KFunction1<MarsRover, Unit>) {
    TURN_LEFT("l", MarsRover::turnLeft),
    TURN_RIGHT("r", MarsRover::turnRight),
    FORWARD("f", MarsRover::moveForward),
    BACKWARD("b", MarsRover::moveBackward);

    open fun applyOver(robot: MarsRover) {
        function.invoke(robot)
    }

    override fun toString(): String {
        return value
    }

    companion object {
        @JvmStatic
        fun movementInstructionOf(value: String): MovementCommandType {
            return Option.fromNullable(values()
                    .firstOrNull { it.value == value })
                    .fold({ throw MarsRoverException("Invalid instruction!") }, { it })
        }
    }
}