package pl.mariusz.marciniak.life

import java.awt.Shape
import java.awt.Color
import java.awt.Graphics2D
import scala.swing.UIElement
import java.awt.Rectangle


class DrawableCell(cell:Cell) {
  private val AliveCellColor = Color.BLUE
  private val DeadCellColor = Color.RED
  
  def shape(ui: UIElement) : Shape = { 
    val horizontalZero : Int = ui.size.getWidth().toInt / 2
    val verticalZero : Int = ui.size.getHeight().toInt / 2
    new Rectangle(horizontalZero+cell.x,verticalZero+cell.y,horizontalZero+cell.x,verticalZero+cell.y)
  }
    
}