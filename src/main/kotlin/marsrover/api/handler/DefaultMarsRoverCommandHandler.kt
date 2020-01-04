package marsrover.api.handler

import marsrover.api.handler.strategy.MovementCommandType.Companion.movementInstructionOf
import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.command.MoveMarsRoverCommand
import marsrover.domain.model.MarsRover
import marsrover.domain.repo.MarsRoverRepository

class DefaultMarsRoverCommandHandler(var repository: MarsRoverRepository) : MarsRoverCommandHandler {

    override fun on(command: MoveMarsRoverCommand) {
        val marsRover = repository.find()
        movementInstructionOf(command.instruction)
                .applyOver(marsRover!!)

        println(String
                .format(
                        "Rover is at x:%d y:%d facing:%s",
                        marsRover.currentPosition.xPoint,
                        marsRover.currentPosition.yPoint,
                        marsRover.currentDirection.value
                ))
    }

    override fun on(command: CreateMarsRoversCommand) {
        val marsRover = MarsRover(
                command.initialDirection,
                command.initialCoordinate,
                command.marsRoverMap)
        repository.save(marsRover)
    }
}