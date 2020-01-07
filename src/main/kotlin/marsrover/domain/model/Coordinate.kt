package marsrover.domain.model

data class Coordinate(var xPoint: Int, var yPoint: Int) {
    operator fun plus(coordinate: Coordinate) =
            Coordinate(this.xPoint + coordinate.xPoint, this.yPoint + coordinate.yPoint)
    operator fun minus(coordinate: Coordinate) =
            Coordinate(this.xPoint - coordinate.xPoint, this.yPoint - coordinate.yPoint)

}




