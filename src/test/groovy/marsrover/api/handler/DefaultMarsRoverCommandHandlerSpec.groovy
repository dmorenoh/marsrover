package marsrover.api.handler

import marsrover.domain.MarsRoverException
import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.command.MoveMarsRoverCommand
import marsrover.domain.model.MarsRover
import marsrover.domain.model.MarsRoverMap
import marsrover.domain.repo.MarsRoverRepository
import marsrover.domain.value.Coordinate
import marsrover.domain.value.MapSize
import marsrover.infra.repo.InMemoryRepository
import spock.lang.Specification
import spock.lang.Unroll

import static marsrover.domain.value.DirectionType.getEnum

class DefaultMarsRoverCommandHandlerSpec extends Specification {
    private static final String NORTH = "n"
    private static final String SOUTH = "s"
    private static final String EAST = "e"
    private static final String WEST = "w"
    private static final String LEFT = "l"
    private static final String RIGHT = "r"
    private static final String NON_VALID = "any"
    private static final Coordinate INITIAL_POS = new Coordinate(0, 0)
    private static final MarsRoverMap ANY_MAP = new MarsRoverMap(new MapSize(2, 2), Collections.emptyList())
    public static final Coordinate OBSTACLE_FORWARD = new Coordinate(0, 1)
    public static final Coordinate OBSTACLE_BACKWARD = new Coordinate(0, 5)
    public static final String FORWARD = "f"
    public static final String BACKWARD = "b"
    private MarsRoverRepository repository = new InMemoryRepository()
    def subject = new DefaultMarsRoverCommandHandler(repository)

    def "should save new mars rover"() {
        when: "request create mars rover"
            def command = new CreateMarsRoversCommand(
                    new MarsRoverMap(new MapSize(5, 5), [OBSTACLE_BACKWARD, OBSTACLE_FORWARD]),
                    new Coordinate(0, 0),
                    getEnum("n"))
            subject.on(command)
        then: "save mars rover with command attributes"
            def saved = repository.find()
            saved.currentPosition == command.initialCoordinate
            saved.currentDirection == command.initialDirection
            saved.map == command.marsRoverMap

    }

    def "should fail when non-valid instruction"() {
        when: "request move mars rovers"
            subject.on(new MoveMarsRoverCommand(NON_VALID))
        then: "fails"
            def ex = thrown(MarsRoverException)
            ex.message == "Invalid instruction!"
    }

    def "should fail when no marsRover found"() {
        when: "request move mars rovers"
            subject.on(new MoveMarsRoverCommand(LEFT))
        then: "fails"
            thrown(NullPointerException)
    }

    @Unroll
    def "should turn mars rover to #expecteDirection when current direction is #currentDirection and requested to move to #movementInstruction"() {
        given: "a mars rover"
            def marsRover = new MarsRover(
                    getEnum(currentDirection),
                    INITIAL_POS,
                    ANY_MAP)
            repository.save(marsRover)

        when: "requested to move"
            subject.on(new MoveMarsRoverCommand(movementInstruction))

        then: "moved correctly"
            marsRover.getCurrentDirection() == getEnum(expecteDirection)
            marsRover.getCurrentPosition() == INITIAL_POS

        where:
            currentDirection | movementInstruction | expecteDirection
            NORTH            | LEFT                | WEST
            NORTH            | RIGHT               | EAST
            SOUTH            | LEFT                | EAST
            SOUTH            | RIGHT               | WEST
            EAST             | LEFT                | NORTH
            EAST             | RIGHT               | SOUTH
            WEST             | LEFT                | SOUTH
            WEST             | RIGHT               | NORTH
    }

    @Unroll
    def "should fail when mars rover attempts to move to #movementInstruction and there is an obstacle"() {
        given: "map with obstacles"
            def mapWithObstacles = new MarsRoverMap(new MapSize(5, 5), [obstaclePosition])
        and: "mars rovers using such map"
            def marsRover = new MarsRover(
                    getEnum(NORTH),
                    INITIAL_POS,
                    mapWithObstacles
            )
            repository.save(marsRover)

        when: "request to move where there is an obstacle"
            subject.on(new MoveMarsRoverCommand(movementInstruction))

        then: "fails and keeps position"
            def ex = thrown(MarsRoverException)
            ex.message == "Obstacle found in next position"
            marsRover.getCurrentPosition() == INITIAL_POS

        where:
            movementInstruction | obstaclePosition
            FORWARD             | OBSTACLE_FORWARD
            BACKWARD            | OBSTACLE_BACKWARD
    }

    @Unroll
    def "should move to #expectedPosition when mars rovers addressed to #direction attempts to move to #movementInstruction and initial position is #initialPosition"() {
        given: "map without obstacles"
            def mapWithObstacles = new MarsRoverMap(new MapSize(5, 5), Collections.emptyList())
        and: "mars rovers using such map"
            def initialDirection = getEnum(direction)
            def marsRover = new MarsRover(
                    initialDirection,
                    initialPosition,
                    mapWithObstacles
            )
            repository.save(marsRover)

        when: "request to move where there is an obstacle"
            subject.on(new MoveMarsRoverCommand(movementInstruction))

        then: "moved"
            marsRover.currentPosition == expectedPosition
            marsRover.currentDirection == initialDirection
        where:
            mapSize           | initialPosition      | direction | movementInstruction | expectedPosition
            new MapSize(5, 5) | new Coordinate(0, 0) | NORTH     | FORWARD             | new Coordinate(0, 1)
            new MapSize(5, 5) | new Coordinate(0, 0) | NORTH     | BACKWARD            | new Coordinate(0, 5)
            new MapSize(5, 5) | new Coordinate(0, 5) | NORTH     | FORWARD             | new Coordinate(0, 0)
            new MapSize(5, 5) | new Coordinate(0, 5) | NORTH     | BACKWARD            | new Coordinate(0, 4)
            new MapSize(5, 5) | new Coordinate(0, 0) | SOUTH     | FORWARD             | new Coordinate(0, 5)
            new MapSize(5, 5) | new Coordinate(0, 0) | SOUTH     | BACKWARD            | new Coordinate(0, 1)
            new MapSize(5, 5) | new Coordinate(0, 5) | SOUTH     | FORWARD             | new Coordinate(0, 4)
            new MapSize(5, 5) | new Coordinate(0, 5) | SOUTH     | BACKWARD            | new Coordinate(0, 0)
    }
}
