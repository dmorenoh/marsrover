package marsrover.domain.repo

import marsrover.domain.model.MarsRover

interface MarsRoverRepository {
    fun find(): MarsRover?
    fun save(marsRover: MarsRover)
}
