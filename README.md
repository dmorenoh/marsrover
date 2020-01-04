# Wallapop Backend Tech Test

Hello candidate!!

Welcome to Wallapop and its backend technical test

## Mars Rover refactoring kata

We need to improve the existent solution that translates commands sent from Earth to instructions that are understood by our Rover in Mars

Currently the code is very complicated and tangled so we want to ask you to invest some time in it

### Functional requirements
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

#### Bonus point

Once ensured the functional requirements have been made, as a bonus point (not necessary but more than welcome), add a new feature:
```
Given:
 - a possible list of obstacles with their exact location
 
When:
 - Rover moves

And:
 - Rover encounters an obstacle

Then:
 - report back the obstacle and keep the position
```

## Your solution

- Change and evolve the provided code as much as you want, but **do not create a new project from scratch**
- Use any JVM language, if you want to use other than Java please convert the provided code in the first commit
- Ensure your solution is self-executable and self-compiled
- Feel free to use any pattern, framework or whatever you want
- Bug free will be a plus
- Fill SOLUTION.md explaining us what you think is important that we know about your solution

## Our evaluation

- We will focus on your design and your own code over frameworks and libraries
- We will take into account the evolution of your solution, from the initial code to your solution
- We will evaluate how complex and difficult is to evolve your solution

## How to do it
This project is a [Template Project](https://help.github.com/en/articles/creating-a-repository-from-a-template) that allows you to create a new project of your own based on this one

We would like you to maintain this new repository as private giving access to `wallabackend` to be able to evaluate it once you are done with your solution

Please, let us know as soon as you finish, otherwise we will not start the review

Thanks & good luck!!
