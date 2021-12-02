# advent-of-code
My Solutions to advent-of-code 2021.  https://adventofcode.com/2021

## Tech stack: 
=================
### Language: Java
### Framework: Spring-boot

## Solution:
=============
### Puzzles
- Solutions are in `src/main/java/se/zettle/adventofcode/puzzle` directory
- Each Puzzle is numbered with respective days. Day1.java, Day2.java ... etc 
- Run respective tests Day1Test.java, Day2Test.java ... etc to get the answers to individual puzzles
- [PuzzleSolverService](src/main/java/se/zettle/adventofcode/service/PuzzleSolverService.java) aggregates all puzzles in one place 

### Inputs
- Inputs are text files in resources `src/main/resources/puzzle/input` directory
- [InputProvider](src/main/java/se/zettle/adventofcode/provider/InputProvider.java) reads all text files in `input`and it's subfolders 
- Pass your inputfilename to `getPuzzleInput` method to get your input as string
  ``` String puzzleInput = InputProvider.getPuzzleInput("day1"); ```
