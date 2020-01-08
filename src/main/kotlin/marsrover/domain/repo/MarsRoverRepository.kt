package marsrover.domain.repo

import arrow.core.Option
import marsrover.domain.model.MarsRover

interface MarsRoverRepository {
    fun find(): Option<MarsRover>
    fun save(marsRover: MarsRover)
}
