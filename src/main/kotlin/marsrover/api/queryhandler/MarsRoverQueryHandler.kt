package marsrover.api.queryhandler

import marsrover.adapter.dto.MarsRoverResponse

interface MarsRoverQueryHandler {
    fun getMarsRover(): MarsRoverResponse
}