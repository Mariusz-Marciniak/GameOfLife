package pl.mariusz.marciniak.life

object GameOfLife {
  type Generation = Set[Cell]
  type Coordinates = Tuple2[Int, Int]

  def importData(data: String): Generation = (data.split("\\n") map (line => Cell(line))).toSet

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
    val beingNeighbourFor = if(n.contains((c.x, c.y))) n((c.x, c.y)) else 0
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
    val f = new GameOfLifeFrame
    f.init
    f.open
    f.startLive(Set(Cell(1,1),Cell(1,2),Cell(2,2),Cell(1,3),Cell(3,3)))
  }
}

