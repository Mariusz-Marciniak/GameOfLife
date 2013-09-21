package pl.mariusz.marciniak.life

object GameOfLife {
  type Generation = List[Cell]

  def importData(data: String): Generation = {
    (for {
      line <- data.split("\\n")
    } yield Cell(line)).toList
    
  }
}

