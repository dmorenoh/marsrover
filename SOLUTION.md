# MY SOLUTION
## Design

 1. Hexagonal architecture split into:
- **Adapters**: In charge of translating communication external consumers with internal api
- **Api**: Or application layer, the one which orchestrates domain capabilities in order to achieve an specific use case: _CreateMarsRover_, _MoveMarsRover_
- **Domain**: Where business logic is contained
- **Infra**: Infrastructure details such as technical capabilities.
**_Why this architecture?_**
Intention of its usage  is to recreate as onion layering responsibilities. This means: Layering dependencies goes in this direction: _domain->api->adapter_, being the domain isolated from api and api isolated from adapter, which means, agnostic of any client consumer. 
**_What we gain with this?_**
 Scalability, modulability, and so on: Imagine at some point you need your application being consumed by a given rest endpoint (representing a new _port_ different than command line) which consumes data from a db. In order to get this, it would only be required to create a new adapter for that rest client and define another repo implementing db connection 

## Implementation
### Key points
### Moving mars rover
-  The _DefaultMarsRoverCommandHandler_ recreates the use case aimed to move a given marsRover. For this specific example, the one on memory. 
- This make usage of _strategy pattern_ in order to apply and specific instruction over _mars rover_.
- The way how strategy pattern has been implemented was by using _Enums_ which I found more configurable and less error prone comparing with switch case version style. 
- This way, you only need to link an specific mars rover instruction (aka _MovementCommandType_) with its respective marsRover action
```
TURN_LEFT("l", MarsRover::turnLeft),  
TURN_RIGHT("r", MarsRover::turnRight),  
FORWARD("f", MarsRover::moveForward),  
BACKWARD("b", MarsRover::moveBackward);
```
###  Mars Rover direction
- On the other side, mars rovers direction affects the way how a given instruction is performed. 

|current Direction | movement Instruction | expected Direction  |
|--|--|--|
|NORTH | LEFT | WEST  |
|NORTH | RIGHT | EAST | 
|SOUTH | LEFT | EAST  |
|SOUTH | RIGHT | WEST  |
|EAST | LEFT | NORTH  |
|EAST | RIGHT | SOUTH  |
|WEST | LEFT | SOUTH  |
|WEST | RIGHT | NORTH|

(* table from above has been used as input when testing this behaviour, please check `DefaultMarsRoverCommandHandlerSpec`)
Something similar happens with forward and backward instruction. Moving forward makes mars rovers moves (x+1, 0) from current position only if is currently pointing to EAST. While, it represents going (0,y+1) if is currently pointing to NORTH.

|current position |current Direction | movement Instruction | expected position  |
|--|--|--|--|
|(0,0)|NORTH|FORWARD|currentPos + (0,1) = (0,1) |
|(0,0)|NORTH|BACKWARD|currentPos - (0,1) = (0,-1) |
|(0,0)|EAST|FORWARD|currentPos + (1,0) = (1,0)|
|(0,0)|WEST|FORWARD|currentPos + (-1,0)= (-1,0)|
|(0,0)|SOUTH|FORWARD|currentPos + (0,-1)= (0,-1)|

.. and so on (again, this table has been used as test coverage in `DefaultMarsRoverCommandHandlerSpec` )
All this means that a _Direction Type_ has its own way to go either LEFT or RIGHT and is pointing to a specific cartesian point (x ,y) when dealing with FORWARD. For example: 
- NORTH turnLeft = WEST 
- NORTH turnRight = EAST
- NORTH directions means going to positive Y ( aka  (0, 1) )

Considering all patterns depicted from above, I've created a new strategy pattern, but this time adding some decorator in order to emulate this capabilities linked to each individual direction type. 
```
enum class DirectionType(
 private val value: String,  
 private val addressedCoordinate: Coordinate) {  
  
  EAST("e", Coordinate(1, 0)) {  
        override fun left() = NORTH  
        override fun right() = SOUTH  
  },  
  
  NORTH("n", Coordinate(0, 1)) {  
        override fun left() = WEST  
        override fun right() = EAST  
  },  
  
  WEST("w", Coordinate(-1, 0)) {  
        override fun left() = SOUTH  
        override fun right() = NORTH  
  },  
  
  SOUTH("s", Coordinate(0, -1)) {  
        override fun left() = EAST  
        override fun right() = WEST  
  };
```
 


