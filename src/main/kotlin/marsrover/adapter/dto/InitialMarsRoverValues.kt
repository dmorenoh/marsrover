package marsrover.adapter.dto

import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.model.*

data class InitialMarsRoverValues(private val mapSizeValue: MapSizeValue,
                                  private val initialPosition: CoordinateValue,
                                  private val initialDirection: String,
                                  private val obstacleValues: List<CoordinateValue>) {
    fun toCreateMarsRoversCommand() = CreateMarsRoversCommand(
            MarsRoverMap(this.mapSizeValue.toArea(), obstacleValues.toObstacles()),
            initialPosition.toCoordinate(),
            DirectionType.getEnum(initialDirection)
    )
}
