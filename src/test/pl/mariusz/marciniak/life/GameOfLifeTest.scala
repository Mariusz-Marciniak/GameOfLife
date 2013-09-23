package pl.mariusz.marciniak.life

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.util.Random
import pl.mariusz.marciniak.life.GameOfLife.Coordinates;
import pl.mariusz.marciniak.life.parser.IncorrectCoordinates

@RunWith(classOf[JUnitRunner])
class GameOfLifeTest extends FunSuite with BeforeAndAfter {

  /*  
The universe of the Game of Life is an infinite two-dimensional orthogonal grid of square cells, each of which is in one of two possible states, alive or dead. 
Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. 
At each step in time, the following transitions occur:
    Any live cell with fewer than two live neighbours dies, as if caused by under-population.
    Any live cell with two or three live neighbours lives on to the next generation.
    Any live cell with more than three live neighbours dies, as if by overcrowding.
    Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
The initial pattern constitutes the seed of the system. The first generation is created by applying the above rules simultaneously to every cell in the seed—births 
and deaths occur simultaneously, and the discrete moment at which this happens is sometimes called a tick (in other words, 
each generation is a pure function of the preceding one). The rules continue to be applied repeatedly to create further generations.
**/

  val sampleGeneration = Set(Cell(1,1),Cell(1,2),Cell(2,2),Cell(1,3),Cell(3,3))
  /**
   * amount of neighbours in generation:
   * 1   1   1
   * 2  {2}  3   1
   * 3  {3} {4}  2   1
   * 2  {2}  4  {1}  1
   * 1   1   2   1   1
   *
   * {x} - alive cell
   * x  - being neighbour for x alive cells
   */
  val amountOfNeighboursInSampleGeneration = Map((0, 0) -> 1, (1, 0) -> 1, (2, 0) -> 1, (0, 1) -> 2, (1, 1) -> 2, (2, 1) -> 3, (3, 1) -> 1,
    (0, 2) -> 3, (1, 2) -> 3, (2, 2) -> 4, (3, 2) -> 2, (4, 2) -> 1,
    (0, 3) -> 2, (1, 3) -> 2, (2, 3) -> 4, (3, 3) -> 1, (4, 3) -> 1,
    (0, 4) -> 1, (1, 4) -> 1, (2, 4) -> 2, (3, 4) -> 1, (4, 4) -> 1)

  test("should create alive cell at any position") {
    val incorrectCoordinates = for {
      x <- -50 to 50 by 3
      y <- -50 to 50 by 7
      if (x != Cell("(" + x + "," + y + ")").x || y != Cell("(" + x + "," + y + ")").y)
    } yield (x, y)

    assert(incorrectCoordinates.size == 0)
  }

  test("should throw exception if coordinates have incorrect format") {
    intercept[IncorrectCoordinates] {
      Cell("[0;0]")
    }
  }

  test("should throw exception if too many coordinates are passed") {
    intercept[IncorrectCoordinates] {
      Cell("(234,33,299)")
    }
  }

  test("should throw exception if any coordinate exceeds Int range") {
    intercept[IncorrectCoordinates] {
      Cell("(2147483648,-2147483649)")
    }
  }

  test("should ignore whitespaces") {
    Cell("  (0,0)   ")
  }

  test("import many lines of coordinates") {
    val importCoordinates = """(0,0)
      (1,0)
      (21,-33)
      (22,-33)"""
    val generation = GameOfLife.importData(importCoordinates)
    assert("Cell(0,0,true),Cell(1,0,true),Cell(21,-33,true),Cell(22,-33,true)" == generation.mkString(","))
  }

  test("import sketch with one element") {
    val sketch = "#"
    val generation = GameOfLife.importData(sketch)
    assert("Cell(0,0,true)" == generation.mkString(","))
  }

  test("enumerate any cell neighbours' coordinates") {
    val cells = for {
      x <- -50 to 50 by 5
      y <- -50 to 50 by 13
    } yield Cell("(" + x + "," + y + ")")

    val filteredCells = cells.filterNot(c => "(" + (c.x - 1) + "," + (c.y - 1) + "),(" + (c.x) + "," + (c.y - 1) + "),(" + (c.x + 1) + "," + (c.y - 1) +
      "),(" + (c.x - 1) + "," + (c.y) + "),(" + (c.x + 1) + "," + (c.y) +
      "),(" + (c.x - 1) + "," + (c.y + 1) + "),(" + (c.x) + "," + (c.y + 1) + "),(" + (c.x + 1) + "," + (c.y + 1) + ")" == c.neighboursCoordinates.mkString(","))

    assert(filteredCells.isEmpty)

  }

  test("collect neighbours for generation") {
    val generation = GameOfLife.importData("""(0,0)
      (1,1)
      (1,0)""")
    val expectedNeighours = Map((-1, -1) -> 1, (-1, 0) -> 1, (-1, 1) -> 1, (0, -1) -> 2, (0, 1) -> 3, (1, -1) -> 2, (1, 0) -> 2, (1, 1) -> 2,
      (0, 0) -> 2, (0, 2) -> 1, (1, 2) -> 1, (2, 0) -> 2, (2, 1) -> 2, (2, 2) -> 1, (2, -1) -> 1)

    val amountOfNeighbours: Map[Coordinates, Int] = GameOfLife.amountOfNeighbours(generation)
    assert((amountOfNeighbours.filterNot { case (key, value) => expectedNeighours(key) == value }).isEmpty)
  }

  test("should die if less than 2 neighbours") {
    val cell = new Cell(3, 3)

    assert(GameOfLife.shouldDie(cell, amountOfNeighboursInSampleGeneration) == true)
  }

  test("should assume 0 neighbours if out of neighbourhood") {
    val cell = new Cell(10, 23)

    assert(GameOfLife.shouldDie(cell, amountOfNeighboursInSampleGeneration) == true)
  }

  test("should stay alive if has 2 neighbours") {
    val cell = new Cell(1, 1)

    assert(GameOfLife.shouldDie(cell, amountOfNeighboursInSampleGeneration) == false)
  }

  test("should stay alive if has 3 neighbours") {
    val cell = new Cell(1, 2)

    assert(GameOfLife.shouldDie(cell, amountOfNeighboursInSampleGeneration) == false)
  }

  test("should die if greater than 3 neighbours") {
    val cell = new Cell(2, 2)

    assert(GameOfLife.shouldDie(cell, amountOfNeighboursInSampleGeneration) == true)
  }

  test("remove dead cells") {
    assert(GameOfLife.removeDeadCells(Set(Cell(1,1), Cell(2,1,false), Cell(3,4, false))).size == 1)
  }

  test ("shouldn't allow duplicates in generation") {
    val generationWithDuplicates : GameOfLife.Generation = Set(Cell(1,1),Cell(1,1,false),Cell(1,1))
    assert(generationWithDuplicates.size == 1)
  }

  test("find all dead cells with exacly 3 neighbours") {
    val offsprings = GameOfLife.findOffsprings(sampleGeneration)
    assert(offsprings.size == 2)
    assert(offsprings.contains((2,1)) == true)
    assert(offsprings.contains((0,2)) == true)
    
  }
  
  test("prepare next generation") {
    val expectedNextGeneration = Set(Cell(1,1),Cell(2,1),Cell(0,2),Cell(1,2),Cell(2,2,false),Cell(1,3),Cell(3,3,false))
    
    assert(expectedNextGeneration.sameElements(GameOfLife.nextGeneration(sampleGeneration)) == true)
  }

}