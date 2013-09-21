package pl.mariusz.marciniak.life

object Cell {
  def apply(cell: String): Cell = {
    val trimmedCell = cell.trim();
    if (!(trimmedCell matches "\\(-{0,1}\\d+,-{0,1}\\d+\\)"))
      throw new IncorrectCoordinates
    
    val coordinates = trimmedCell replaceAll ("[()]", "") split (",")
    try {
        new Cell(coordinates(0).toInt, coordinates(1).toInt)
    } catch {
      case e: NumberFormatException => throw new IncorrectCoordinates 
    }
  }

}
case class Cell(x: Int, y: Int, isAlive: Boolean = true) {
    def neighbours() : List[Tuple2[Int,Int]] = {
      List((x-1,y-1),(x,y-1),(x+1,y-1),(x-1,y),(x+1,y),(x-1,y+1),(x,y+1),(x+1,y+1))
    }
}

