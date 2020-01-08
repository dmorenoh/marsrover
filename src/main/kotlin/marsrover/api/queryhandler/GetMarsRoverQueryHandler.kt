package marsrover.api.queryhandler

import marsrover.adapter.dto.MarsRoverResponse
import marsrover.domain.exception.MarsRoverException
import marsrover.domain.repo.MarsRoverRepository

class GetMarsRoverQueryHandler(private val repository: MarsRoverRepository) : MarsRoverQueryHandler {
    override fun getMarsRover(): MarsRoverResponse {
        val marsRover = repository.find()
                .fold({ throw MarsRoverException("Mars rover not found!") }, { it })
        return MarsRoverResponse.of(marsRover)
    }
}