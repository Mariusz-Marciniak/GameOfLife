package pl.mariusz.marciniak.life

object Cell {
  def apply(cell: String):Cell = {
    new Cell(0,0)
  }
}
case class Cell(x:Int, y:Int, isAlive: Boolean = true)  {

}

