package marsrover.domain.command

import marsrover.domain.value.Coordinate
import marsrover.domain.value.DirectionType
import marsrover.domain.model.MarsRoverMap

data class CreateMarsRoversCommand(var marsRoverMap: MarsRoverMap,
                                   var initialCoordinate: Coordinate,
                                   var initialDirection: DirectionType)

data class MoveMarsRoverCommand(var instruction: String)
