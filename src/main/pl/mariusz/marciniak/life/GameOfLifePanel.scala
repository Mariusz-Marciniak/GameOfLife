package pl.mariusz.marciniak.life

import scala.swing.BorderPanel
import java.awt.Color
import pl.mariusz.marciniak.life.GameOfLife.Generation
import java.awt.Graphics2D
import java.awt.Rectangle

class GameOfLifePanel extends BorderPanel {

  private var gen : Generation = _
  
  private var gp : GraphicalPresenter = _
  
  def init = {
    background = Color.WHITE
    gp = GraphicalPresenter(this)
  }

  def generation_=(g: Generation):Unit = gen = g
  
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g);
    gen foreach {case c => g.setColor(gp.color(c)); g.fill(gp.shape(c)) }
  }

}