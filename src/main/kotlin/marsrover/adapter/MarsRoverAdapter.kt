package marsrover.adapter

import marsrover.adapter.dto.InitialMarsRoverValues
import marsrover.api.handler.MarsRoverCommandHandler
import marsrover.domain.command.MoveMarsRoverCommand

class MarsRoverAdapter(private val handlerDefault: MarsRoverCommandHandler) {

    fun initializeMarsRover(initialMarsRoverValues: InitialMarsRoverValues) {
        handlerDefault.on(initialMarsRoverValues.toCreateMarsRoversCommand())
    }

    fun moveMarsRover(command: String) {
        handlerDefault.on(MoveMarsRoverCommand(command))
    }
}