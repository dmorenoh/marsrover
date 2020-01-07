package marsrover.adapter.dto

import marsrover.domain.model.Coordinate

data class CoordinateValue(private val positionX: Int,
                           private val positionY: Int) {
    fun toCoordinate(): Coordinate = Coordinate(this.positionX, this.positionY)
}