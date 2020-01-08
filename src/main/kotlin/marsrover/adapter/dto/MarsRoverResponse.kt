package marsrover.adapter.dto

import marsrover.domain.model.MarsRover

data class MarsRoverResponse(private val direction: String,
                             private val position: String) {
    override fun toString(): String {
        return String
                .format("Rover is at %s facing %s", position, direction)
    }

    companion object {
        fun of(marsRover: MarsRover): MarsRoverResponse {
            return MarsRoverResponse(
                    marsRover.currentDirectionAsString(),
                    marsRover.currentPositionAsString())
        }
    }
}