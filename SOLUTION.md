# MY SOLUTION
## Design

 1. Hexagonal architecture split into:
- **Adapters**: In charge of enhance communication between external consumers and internal api
- **Api**: Or application layer, the one which orchestrates domain capabilities in order to achieve an specific use case: _CreateMarsRover_, _MoveMarsRover_,_GetMarsRover_
- **Domain**: Where business logic is contained
- **Infra**: Infrastructure details such as technical capabilities: in memory repo implementation for this case.

**_Why this architecture?_**
 Intention of its usage  is to recreate an onion layering responsibilities. This implies layering dependencies in this direction: _domain->api->adapter_, being the domain isolated from api and api isolated from adapter, which means, agnostic of any client consumer. 

**_What we gain with this?_**
 Scalability, modulability, separation of concerns and so on: Imagine at some point you need your application being consumed by a given rest endpoint (representing a new _port_ different than command line) which consumes data from a real db. In order to get this, it would only be required to create a new adapter for that rest client and define another repo implementing db connection 

## Implementation
### Key points
### Moving mars rover
-  The _DefaultMarsRoverCommandHandler_ recreates the use case aimed to move a given marsRover. 
- This make usage of _strategy pattern_ in order to apply and specific instruction over _mars rover_.
- The way how strategy pattern has been implemented was by using _Enums_ which I found more configurable and less error prone comparing with switch case version style. 
- This way, you only need to link an specific mars rover instruction (aka _MovementCommandType_) with its respective marsRover action
```
TURN_LEFT("l", MarsRover::turnLeft),  
TURN_RIGHT("r", MarsRover::turnRight),  
FORWARD("f", MarsRover::moveForward),  
BACKWARD("b", MarsRover::moveBackward);
```

## Tech tools
- Kotlin (not so expert in this as in java but I wanted to give a try)
- Spock for testing

###  Mars Rover direction
- On the other side, mars rovers direction affects the way how a given instruction is performed. 

|initial Direction | movement Instruction | final Direction  |
|--|--|--|
|NORTH | LEFT | WEST  |
|NORTH | RIGHT | EAST | 
|SOUTH | LEFT | EAST  |
|SOUTH | RIGHT | WEST  |
|EAST | LEFT | NORTH  |
|EAST | RIGHT | SOUTH  |
|WEST | LEFT | SOUTH  |
|WEST | RIGHT | NORTH|

(* table from above has been used as input when testing this behaviour, please check [`DefaultMarsRoverCommandHandlerSpec`](https://github.com/dmorenoh/marsrover/blob/master/src/test/groovy/marsrover/api/commandhandler/DefaultMarsRoverCommandHandlerSpec.groovy))
Something similar happens with _forward_ and _backward_ instruction. Moving forward makes mars rovers moves (x+1, 0) from current position only if is currently pointing to EAST. While, it represents going (0,y+1) if is currently pointing to NORTH for instance. Below table shows this relationship between direction and heading position.

|initial position |current Direction | movement Instruction | final position  |
|--|--|--|--|
|(0,0)|NORTH|FORWARD|currentPos + (0,1) = (0,1) |
|(0,0)|NORTH|BACKWARD|currentPos - (0,1) = (0,-1) |
|(0,0)|EAST|FORWARD|currentPos + (1,0) = (1,0)|
|(0,0)|WEST|FORWARD|currentPos + (-1,0)= (-1,0)|
|(0,0)|SOUTH|FORWARD|currentPos + (0,-1)= (0,-1)|

.. and so on (again, this table has been used as test coverage in [`DefaultMarsRoverCommandHandlerSpec`](https://github.com/dmorenoh/marsrover/blob/master/src/test/groovy/marsrover/api/commandhandler/DefaultMarsRoverCommandHandlerSpec.groovy) )
All this means that an specific _Direction Type_ has its own way to go either LEFT or RIGHT and is pointing to a specific cartesian point (x ,y) when dealing with FORWARD. For example: 
- NORTH turnLeft = WEST 
- NORTH turnRight = EAST
- NORTH direction means addressing to positive Y ( aka  (0, 1) )

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

## Test evidence to be considered
I've decided to keep the initial class `MarsRover.java` as input (port) point and focus on the core-domain solution. The way how this initial class communication with my api is through adapters (`MarsRoveAdapter`)
Considering your initial test for main functional requirement 
```
Given:
 - a map size of two dimensions for Mars
 - the initial starting point and direction of the Rover
When:
 - a command is received
   move `forward` or `backward` or rotate `left` or `right` (90 degrees)
Then:
 - move the Rover
   if arrives at one of the edges follow at the other side (Mars is a sphere after all)
```
It has been considered in order to create [`DefaultMarsRoverCommandHandlerSpec`](https://github.com/dmorenoh/marsrover/blob/master/src/test/groovy/marsrover/api/commandhandler/DefaultMarsRoverCommandHandlerSpec.groovy)
Please check this scenarios:
- `def "should turn mars rover to #expecteDirection when initial direction is #currentDirection and requested to move to #movementInstruction"() `
- `def "should move to #expectedPosition when mars rovers addressed to #direction attempts to move to #movementInstruction and initial position is #initialPosition"()`

On the other hand, bonus about _obstacles_ has been implemented having as test coverage (in the same file) this scenario:
- `def "should fail when mars rover attempts to move to #movementInstruction and there is an obstacle"()`

In order to have a fancy way to check test results, reports has been provided. So, once you download the project, please check in:
`build/reports/tests/test/classes/marsrover.api.commandhandler.DefaultMarsRoverCommandHandlerSpec.html`
It should be recreated every time you execute Spock tests

## Technical debts
Input validation when creating parameters to adapter



 


