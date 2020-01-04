package marsrover.adapter.dto

data class InitialMarsRoverValues(val mapSizeValue: MapSizeValue,
                                  val initialPosition: CoordinateValue,
                                  val initialDirection: String,
                                  val obstacleValues: List<CoordinateValue>)

data class MapSizeValue(val maxX: Int = 0, val maxY: Int = 0)
data class CoordinateValue(val positionX: Int, val positionY: Int)