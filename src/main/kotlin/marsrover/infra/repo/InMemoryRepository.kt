package marsrover.infra.repo

import marsrover.domain.model.MarsRover
import marsrover.domain.repo.MarsRoverRepository

class InMemoryRepository(var inMemoryMarsRover: MarsRover?) : MarsRoverRepository {
    override fun find(): MarsRover? {
        return inMemoryMarsRover
    }

    override fun save(marsRover: MarsRover) {
        inMemoryMarsRover = marsRover
    }
}