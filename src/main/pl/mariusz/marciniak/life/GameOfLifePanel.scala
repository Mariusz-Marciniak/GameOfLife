package pl.mariusz.marciniak.life

import scala.swing.BorderPanel
import java.awt.Color
import pl.mariusz.marciniak.life.GameOfLife.Generation
import java.awt.Graphics2D
import java.awt.Rectangle

class GameOfLifePanel extends BorderPanel {

  private var gen : Set[DrawableCell] = _
  
  
  def init = {
    background = Color.WHITE
  }
  
  def generation_=(g: Set[DrawableCell]):Unit = gen = g
  
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g);
  }

}