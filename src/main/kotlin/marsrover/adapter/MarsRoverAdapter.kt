package marsrover.adapter

import marsrover.adapter.dto.InitialMarsRoverValues
import marsrover.api.commandhandler.MarsRoverCommandHandler
import marsrover.api.queryhandler.GetMarsRoverQueryHandler
import marsrover.api.queryhandler.MarsRoverQueryHandler
import marsrover.domain.command.MoveMarsRoverCommand

class MarsRoverAdapter(private val commandHandler: MarsRoverCommandHandler,
                       private val queryHandler: MarsRoverQueryHandler) {

    fun initializeMarsRover(initialMarsRoverValues: InitialMarsRoverValues) =
            commandHandler.on(initialMarsRoverValues.toCreateMarsRoversCommand())

    fun moveMarsRover(command: String) = commandHandler.on(MoveMarsRoverCommand(command))

    fun getMarsRovers() = queryHandler.getMarsRover()
}