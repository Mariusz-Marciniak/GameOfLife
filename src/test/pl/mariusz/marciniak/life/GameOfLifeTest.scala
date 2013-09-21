package pl.mariusz.marciniak.life

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

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

  test("center cell neighbours") {
    val centerCell = Cell("(0,0)")
    assert("(-1,-1),(0,-1),(1,-1),(-1,0),(1,0),(-1,1),(0,1),(1,1)"==centerCell.neighbours.mkString(","))
  }

}