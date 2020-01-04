package marsrover.api.handler

import marsrover.domain.command.CreateMarsRoversCommand
import marsrover.domain.command.MoveMarsRoverCommand

interface MarsRoverCommandHandler {
    fun on(command: MoveMarsRoverCommand)
    fun on(command: CreateMarsRoversCommand)
}