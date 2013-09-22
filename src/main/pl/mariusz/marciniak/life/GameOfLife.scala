package pl.mariusz.marciniak.life

object GameOfLife {
  type Generation = List[Cell]
  type Coordinates = Tuple2[Int,Int]

  def importData(data: String): Generation = {
    (for {
      line <- data.split("\\n")
    } yield Cell(line)).toList

  }

  def amountOfTimesBeingNeighbour(g: Generation): Map[Coordinates, Int] = {
      val generationNeighbours = for {
        cell <- g
        if(cell.isAlive)
        neighbour <- cell.neighboursCoordinates   
      } yield neighbour 
      (for { 
        coordinate <- generationNeighbours.distinct
      } yield (coordinate,generationNeighbours.count(x => x._1==coordinate._1 && x._2==coordinate._2))).toMap
  }
  
  def shouldDie(c: Cell, n: Map[Coordinates, Int]) : Boolean = {
    val beingNeighbourFor = n((c.x,c.y))
    underPopulation(beingNeighbourFor) || overPopulation(beingNeighbourFor)
  }
  
  def underPopulation(beingNeighbourFor: Int) : Boolean = beingNeighbourFor < 2

  def overPopulation(beingNeighbourFor: Int) : Boolean = beingNeighbourFor > 3
}

