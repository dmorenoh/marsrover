package marsrover.adapter.dto
import marsrover.domain.model.Coordinate
import java.util.stream.Collectors


fun List<CoordinateValue>.toObstacles(): List<Coordinate> = this.stream().map { it.toCoordinate() }.collect(Collectors.toList())

