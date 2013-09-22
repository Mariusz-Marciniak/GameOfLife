package pl.mariusz.marciniak.life

import java.awt.Dimension
import scala.swing.UIElement
import java.awt.Color
import java.awt.Shape
import java.awt.Rectangle

object GraphicalPresenter {
  def apply(ui: UIElement): GraphicalPresenter = {
    val hZ: Int = ui.size.getWidth().toInt / 2
    val vZ: Int = ui.size.getHeight().toInt / 2
    new GraphicalPresenter(hZ, vZ)
  }
}

class GraphicalPresenter(val horizontalZero: Int, val verticalZero: Int) {
  val AliveCellColor = Color.BLUE
  val DeadCellColor = Color.RED
  val PointSize = 3

  def color(cell: Cell): Color = if (cell.isAlive) AliveCellColor else DeadCellColor

  def shape(cell: Cell): Shape =
    new Rectangle(horizontalZero + cell.x*PointSize, verticalZero + cell.y*PointSize, PointSize, PointSize)

}