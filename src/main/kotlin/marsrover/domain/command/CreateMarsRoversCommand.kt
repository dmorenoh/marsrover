package marsrover.domain.command

import marsrover.domain.model.Coordinate
import marsrover.domain.model.DirectionType
import marsrover.domain.model.MarsRoverMap

data class CreateMarsRoversCommand(var marsRoverMap: MarsRoverMap,
                                   var initialCoordinate: Coordinate,
                                   var initialDirection: DirectionType)