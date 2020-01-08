package marsrover.adapter

import marsrover.adapter.dto.CoordinateValue
import marsrover.adapter.dto.InitialMarsRoverValues
import marsrover.adapter.dto.MapSizeValue
import marsrover.api.commandhandler.MarsRoverCommandHandler
import marsrover.api.queryhandler.MarsRoverQueryHandler
import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.exception.MarsRoverException
import marsrover.domain.model.*
import spock.lang.Specification

class MarsRoverAdapterTest extends Specification {


    private static final int MAX_X = 5
    private static final int MAX_Y = 5
    private static final CoordinateValue INITIAL_POS_VALUE = new CoordinateValue(0, 0)
    private static final String SOUTH = "s"
    private static final String INVALID = "any"
    private static final CoordinateValue OBSTACLE_POS = new CoordinateValue(1, 1)
    MarsRoverCommandHandler commandHandler = Mock()
    MarsRoverQueryHandler queryHandler = Mock()
    MarsRoverAdapter marsRoverAdapter = new MarsRoverAdapter(commandHandler, queryHandler)

    def "should fails when invalid direction requested"() {
        given: "a request"
            def request = new InitialMarsRoverValues(
                    new MapSizeValue(MAX_X, MAX_Y),
                    INITIAL_POS_VALUE,
                    INVALID,
                    [OBSTACLE_POS]
            )
        when: "request to create mars rover"
            marsRoverAdapter.initializeMarsRover(request)

        then: 'fails'
            MarsRoverException ex = thrown()
            ex.message == "Invalid direction!"
    }

    def "should execute create mars rover"() {
        given: "a request"
            def request = new InitialMarsRoverValues(
                    new MapSizeValue(MAX_X, MAX_Y),
                    INITIAL_POS_VALUE,
                    SOUTH,
                    [OBSTACLE_POS]
            )
        when: "request to create mars rover"
            marsRoverAdapter.initializeMarsRover(request)

        then: 'execute command'
            1 * commandHandler.on({ CreateMarsRoversCommand command ->
                command.initialDirection == DirectionType.of(SOUTH)
                command.initialCoordinate == new Coordinate(
                        INITIAL_POS_VALUE.positionX,
                        INITIAL_POS_VALUE.positionY)
                command.marsRoverMap == new MarsRoverMap(
                        new Area(new Size(MAX_X), new Size(MAX_Y)),
                        [new Coordinate(OBSTACLE_POS.positionX, OBSTACLE_POS.positionY)])
            })
    }
}
