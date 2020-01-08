package marsrover.api.queryhandler

import arrow.core.Option
import marsrover.domain.exception.MarsRoverException
import marsrover.domain.model.*
import marsrover.domain.repo.MarsRoverRepository
import spock.lang.Specification

import static marsrover.domain.model.DirectionType.of

class GetMarsRoverQueryHandlerSpec extends Specification {
    private MarsRoverRepository repository = Mock()
    def subject = new GetMarsRoverQueryHandler(repository)
    private static final DirectionType ANY_DIRECTION = of("n")
    private static final Coordinate INITIAL_POS = new Coordinate(0, 0)
    private static final MarsRoverMap ANY_MAP = new MarsRoverMap(new Area(new Size(2), new Size(2)), Collections.emptyList())

    def "should fail when no mars rover found"() {
        given: "not any mars rover"
            repository.find() >> Option.@Companion.empty()
        when: "gets mars rover"
            subject.getMarsRover()
        then: "fails"
            def ex = thrown(MarsRoverException)
            ex.message == "Mars rover not found!"
    }

    def "should return mars rover info when mars rover found"() {
        given: "a mars rover"
            def marsRover = new MarsRover(
                    ANY_DIRECTION,
                    INITIAL_POS,
                    ANY_MAP)
            repository.find() >> Option.@Companion.just(marsRover)

        when: "gets mars rover"
            def response = subject.getMarsRover()

        then: "returns mars rover info"
            response.direction == ANY_DIRECTION.name()
            response.position == INITIAL_POS.toString()
    }
}
