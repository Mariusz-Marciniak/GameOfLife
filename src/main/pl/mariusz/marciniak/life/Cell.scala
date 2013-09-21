package pl.mariusz.marciniak.life

object Cell {
  def apply(cell: String): Cell = {
    if (!(cell matches "\\(-{0,1}\\d+,-{0,1}\\d+\\)"))
      throw new IncorrectCoordinates
    
    val coordinates = cell replaceAll ("[()]", "") split (",")
    new Cell(coordinates(0).toInt, coordinates(1).toInt)
  }

}
case class Cell(x: Int, y: Int, isAlive: Boolean = true) {

}

