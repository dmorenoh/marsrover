package marsrover.adapter.dto

import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.value.Coordinate
import marsrover.domain.value.DirectionType
import marsrover.domain.value.MapSize
import marsrover.domain.model.MarsRoverMap
import java.util.stream.Collectors

fun InitialMarsRoverValues.toCreateMarsRoversCommand() = CreateMarsRoversCommand(
        MarsRoverMap(this.mapSizeValue.toMapSize(), obstacleValues.toObstacles()),
        this.initialPosition.toCoordinate(),
        DirectionType.getEnum(this.initialDirection)
)

fun MapSizeValue.toMapSize(): MapSize = MapSize(this.maxX, this.maxY)

fun List<CoordinateValue>.toObstacles(): List<Coordinate> = this.stream().map { it.toCoordinate() }.collect(Collectors.toList())

fun CoordinateValue.toCoordinate(): Coordinate = Coordinate(this.positionX, this.positionY)