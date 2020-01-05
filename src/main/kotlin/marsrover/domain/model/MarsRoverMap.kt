package marsrover.domain.model

import marsrover.domain.value.Coordinate
import marsrover.domain.value.MapSize

data class MarsRoverMap(var mapSize: MapSize,
                        var obstacles: List<Coordinate>)