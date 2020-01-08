package marsrover.infra.repo

import arrow.core.Option
import marsrover.domain.model.MarsRover
import marsrover.domain.repo.MarsRoverRepository

class InMemoryRepository : MarsRoverRepository {

    companion object {
        private var instance: MarsRover? = null
    }

    override fun find(): Option<MarsRover> {
        return Option.fromNullable(instance)
    }

    override fun save(marsRover: MarsRover) {
        instance = marsRover
    }
}