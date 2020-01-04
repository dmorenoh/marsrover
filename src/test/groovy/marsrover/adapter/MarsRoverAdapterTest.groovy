package marsrover.adapter


import marsrover.adapter.dto.CoordinateValue
import marsrover.adapter.dto.InitialMarsRoverValues
import marsrover.adapter.dto.MapSizeValue
import marsrover.api.handler.MarsRoverCommandHandler
import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.model.MarsRoverMap
import marsrover.domain.value.Coordinate
import marsrover.domain.value.DirectionType
import marsrover.domain.value.MapSize
import spock.lang.Specification
import spock.lang.Unroll

class MarsRoverAdapterTest extends Specification {


    public static final int MAX_X = 5
    public static final int MAX_Y = 5
    public static final CoordinateValue INITIAL_POS_VALUE = new CoordinateValue(0, 0)
    public static final String INITIAL_DIR = "s"
    public static final CoordinateValue OBSTACLE_POS = new CoordinateValue(1, 1)
    MarsRoverCommandHandler handler = Mock()
    MarsRoverAdapter marsRoverAdapter = new MarsRoverAdapter(handler)

    def "should execute create mars rover"() {
        given: "a request"
            def request = new InitialMarsRoverValues(
                    new MapSizeValue(MAX_X, MAX_Y),
                    INITIAL_POS_VALUE,
                    INITIAL_DIR,
                    [OBSTACLE_POS]
            )
        when: "request to create mars rover"
            marsRoverAdapter.initializeMarsRover(request)
        then: 'execute command'
            1 * handler.on(new CreateMarsRoversCommand(
                    new MarsRoverMap(new MapSize(MAX_X, MAX_Y), [new Coordinate(OBSTACLE_POS.positionX, OBSTACLE_POS.positionY)]),
                    new Coordinate(INITIAL_POS_VALUE.positionX, INITIAL_POS_VALUE.positionY),
                    DirectionType.getEnum(INITIAL_DIR)))
    }
}
