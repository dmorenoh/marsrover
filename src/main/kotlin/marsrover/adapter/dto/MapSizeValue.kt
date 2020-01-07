package marsrover.adapter.dto

import marsrover.domain.model.Area
import marsrover.domain.model.Size

data class MapSizeValue(private val maxX: Int = 0,
                        private val maxY: Int = 0) {
    fun toArea(): Area = Area(Size(this.maxX), Size(this.maxY))
}