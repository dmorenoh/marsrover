package marsrover.api.commandhandler

import arrow.core.Option
import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.command.MoveMarsRoverCommand
import marsrover.domain.exception.MarsRoverMapException
import marsrover.domain.exception.MarsRoverException
import marsrover.domain.model.*
import marsrover.domain.repo.MarsRoverRepository
import spock.lang.Specification
import spock.lang.Unroll

import static marsrover.domain.model.DirectionType.of

class DefaultMarsRoverCommandHandlerSpec extends Specification {
    private static final String NORTH = "n"
    private static final String SOUTH = "s"
    private static final String EAST = "e"
    private static final String WEST = "w"
    private static final String LEFT = "l"
    private static final String RIGHT = "r"
    private static final String NON_VALID = "any"
    private static final Coordinate INITIAL_POS = new Coordinate(0, 0)
    private static final MarsRoverMap ANY_MAP = new MarsRoverMap(new Area(new Size(2), new Size(2)), Collections.emptyList())
    private static final Coordinate OBSTACLE_FORWARD = new Coordinate(0, 1)
    private static final Coordinate OBSTACLE_BACKWARD = new Coordinate(0, 5)
    private static final String FORWARD = "f"
    private static final String BACKWARD = "b"
    private static final Area AREA_SIZE_5X5 = new Area(new Size(5), new Size(5))
    private static final DirectionType ANY_DIRECTION = of("n")
    private MarsRoverRepository repository = Mock()
    def subject = new DefaultMarsRoverCommandHandler(repository)

    def "should save new mars rover"() {
        when: "request create mars rover"
            def command = new CreateMarsRoversCommand(
                    ANY_MAP,
                    INITIAL_POS,
                    ANY_DIRECTION)
            subject.on(command)

        then: "save mars rover with command attributes"
            1 * repository.save({ MarsRover marsRover ->
                marsRover.currentPosition == INITIAL_POS
                marsRover.currentDirection == ANY_DIRECTION
                marsRover.map == ANY_MAP
            })
    }

    def "should fail when non-valid instruction"() {
        given: "a mars rover"
            def marsRover = new MarsRover(
                    ANY_DIRECTION,
                    INITIAL_POS,
                    ANY_MAP)
            repository.find() >> Option.@Companion.just(marsRover)

        when: "request move mars rovers"
            subject.on(new MoveMarsRoverCommand(NON_VALID))
        then: "fails"
            MarsRoverException ex = thrown()
            ex.message == "Invalid instruction!"
    }

    def "should fail when no marsRover found"() {
        given: "not any mars rover"
            repository.find() >> Option.@Companion.empty()
        when: "request move mars rovers"
            subject.on(new MoveMarsRoverCommand(LEFT))
        then: "fails"
            MarsRoverException ex = thrown()
            ex.message == "Mars rover not found!"
    }

    @Unroll
    def "should turn mars rover to #expecteDirection when initial direction is #currentDirection and requested to move to #movementInstruction"() {
        given: "a mars rover"
            def marsRover = new MarsRover(
                    currentDirection,
                    INITIAL_POS,
                    ANY_MAP)
            repository.find() >> Option.@Companion.just(marsRover)

        when: "requested to move"
            subject.on(new MoveMarsRoverCommand(movementInstruction))

        then: "moved correctly"
            marsRover.currentDirection == expecteDirection
            marsRover.currentPosition == INITIAL_POS

        where:
            currentDirection | movementInstruction | expecteDirection
            of(NORTH)        | LEFT                | of(WEST)
            of(NORTH)        | RIGHT               | of(EAST)
            of(SOUTH)        | LEFT                | of(EAST)
            of(SOUTH)        | RIGHT               | of(WEST)
            of(EAST)         | LEFT                | of(NORTH)
            of(EAST)         | RIGHT               | of(SOUTH)
            of(WEST)         | LEFT                | of(SOUTH)
            of(WEST)         | RIGHT               | of(NORTH)
    }

    @Unroll
    def "should fail when mars rover attempts to move to #movementInstruction and there is an obstacle"() {
        given: "map with obstacles"
            def mapWithObstacles = new MarsRoverMap(AREA_SIZE_5X5, [obstaclePosition])
        and: "mars rovers using such map"
            def marsRover = new MarsRover(
                    of(NORTH),
                    INITIAL_POS,
                    mapWithObstacles
            )
            repository.find() >> Option.@Companion.just(marsRover)

        when: "request to move where there is an obstacle"
            subject.on(new MoveMarsRoverCommand(movementInstruction))

        then: "fails and keeps position"
            MarsRoverMapException ex = thrown()
            ex.message == "Obstacle found in next position"
            marsRover.currentPosition == INITIAL_POS

        where:
            movementInstruction | obstaclePosition
            FORWARD             | OBSTACLE_FORWARD
            BACKWARD            | OBSTACLE_BACKWARD
    }

    @Unroll
    def "should move to #expectedPosition when mars rovers addressed to #direction attempts to move to #movementInstruction and initial position is #initialPosition"() {
        given: "map without obstacles"
            def mapWithObstacles = new MarsRoverMap(AREA_SIZE_5X5, Collections.emptyList())
        and: "mars rovers using such map"
            def initialDirection = direction
            def marsRover = new MarsRover(
                    initialDirection,
                    initialPosition,
                    mapWithObstacles
            )
            repository.find() >> Option.@Companion.just(marsRover)

        when: "request to move where there is an obstacle"
            subject.on(new MoveMarsRoverCommand(movementInstruction))

        then: "moved"
            marsRover.currentPosition == expectedPosition
            marsRover.currentDirection == initialDirection
        where:
            mapSize       | initialPosition      | direction | movementInstruction | expectedPosition
            AREA_SIZE_5X5 | new Coordinate(0, 0) | of(NORTH) | FORWARD             | new Coordinate(0, 1)
            AREA_SIZE_5X5 | new Coordinate(0, 0) | of(NORTH) | BACKWARD            | new Coordinate(0, 5)
            AREA_SIZE_5X5 | new Coordinate(0, 5) | of(NORTH) | FORWARD             | new Coordinate(0, 0)
            AREA_SIZE_5X5 | new Coordinate(0, 5) | of(NORTH) | BACKWARD            | new Coordinate(0, 4)
            AREA_SIZE_5X5 | new Coordinate(0, 0) | of(SOUTH) | FORWARD             | new Coordinate(0, 5)
            AREA_SIZE_5X5 | new Coordinate(0, 0) | of(SOUTH) | BACKWARD            | new Coordinate(0, 1)
            AREA_SIZE_5X5 | new Coordinate(0, 5) | of(SOUTH) | FORWARD             | new Coordinate(0, 4)
            AREA_SIZE_5X5 | new Coordinate(0, 5) | of(SOUTH) | BACKWARD            | new Coordinate(0, 0)
    }
}
