package marsrover.api.commandhandler

import marsrover.api.commandhandler.strategy.MovementCommandType.Companion.movementInstructionOf
import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.command.MoveMarsRoverCommand
import marsrover.domain.exception.MarsRoverException
import marsrover.domain.model.MarsRover
import marsrover.domain.repo.MarsRoverRepository

class DefaultMarsRoverCommandHandler(private var repository: MarsRoverRepository) : MarsRoverCommandHandler {

    override fun on(command: MoveMarsRoverCommand) {
        val marsRover = repository.find()
                .fold({ throw MarsRoverException("Mars rover not found!") }, { it })

        movementInstructionOf(command.instruction)
                .applyOver(marsRover)
    }

    override fun on(command: CreateMarsRoversCommand) {
        val marsRover = MarsRover(
                command.initialDirection,
                command.initialCoordinate,
                command.marsRoverMap)
        repository.save(marsRover)
    }
}