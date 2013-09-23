package pl.mariusz.marciniak.life

import scala.io.Source
import java.io.FileNotFoundException
import java.io.File
import pl.mariusz.marciniak.life.parser.IncorrectCoordinates
import pl.mariusz.marciniak.life.parser.DataParser
import pl.mariusz.marciniak.life.parser.ParsingException

object GameOfLife {
  type Generation = Set[Cell]
  type Coordinates = Tuple2[Int, Int]

  def importData(data: String): Generation = DataParser.importData(data)

  def findOffsprings(g: Generation): Set[Coordinates] = findOffsprings(removeDeadCells(g), amountOfNeighbours(g))

  def findOffsprings(aliveCells: Generation, neighboursAmount: Map[Coordinates, Int]): Set[Coordinates] = {
    (neighboursAmount filter { case (coordinate, neighbours) => neighbours == 3 && !aliveCells.contains(Cell(coordinate._1, coordinate._2)) }).keySet
  }

  def removeDeadCells(g: Generation): Generation = g.filter(c => c.isAlive)

  def amountOfNeighbours(g: Generation): Map[Coordinates, Int] = {
    val generationNeighbours: List[Coordinates] = for {
      cell <- removeDeadCells(g).toList
      neighbour <- cell.neighboursCoordinates
    } yield neighbour
    (for {
      coordinate <- generationNeighbours.distinct
    } yield (coordinate, generationNeighbours.count(x => x._1 == coordinate._1 && x._2 == coordinate._2))).toMap
  }

  def shouldDie(c: Cell, n: Map[Coordinates, Int]): Boolean = {
    val beingNeighbourFor = if (n.contains((c.x, c.y))) n((c.x, c.y)) else 0
    underPopulation(beingNeighbourFor) || overPopulation(beingNeighbourFor)
  }

  def underPopulation(beingNeighbourFor: Int): Boolean = beingNeighbourFor < 2

  def overPopulation(beingNeighbourFor: Int): Boolean = beingNeighbourFor > 3

  def nextGeneration(g: Generation): Generation = {
    val aliveCells = removeDeadCells(g)
    val neighbours = amountOfNeighbours(g)
    val offsprings = findOffsprings(aliveCells, neighbours)
    val divideCells = aliveCells.partition(shouldDie(_, neighbours))
    killCells(divideCells._1) ++ divideCells._2 ++ bornCells(offsprings)
  }

  def killCells(g: Generation): Generation = g.map(c => new Cell(c.x, c.y, false))

  def bornCells(c: Set[Coordinates]): Generation = c map { case (x, y) => new Cell(x, y) }

  def main(args: Array[String]) {
    if (args.length > 0) {
      try {
        val file = new File(".")
        val data = Source.fromFile(args(0)).mkString
        val generation = importData(data)
        val f = new GameOfLifeFrame
        f.init
        f.open
        f.startLive(generation)
      } catch {
        case e: FileNotFoundException => println("File " + args(0) + " not found")
        case e: ParsingException => println(e.message)
        case e: IncorrectCoordinates => println("Incorrect coordinates in file " + args(0))
      }
    } else {
      println("Required file name")
    }
  }
}

