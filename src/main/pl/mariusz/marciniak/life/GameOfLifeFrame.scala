package pl.mariusz.marciniak.life

import scala.swing.MainFrame
import pl.mariusz.marciniak.life.GameOfLife.Generation
import java.awt.Dimension

class GameOfLifeFrame extends MainFrame {
  val panel = new GameOfLifePanel

  def init = {
    preferredSize = new Dimension(600, 600)
    title = "Game of Life"
    contents_=(panel)
    panel.init
  }

  def startLive(g: Generation) = {
    def loop(g: Generation): Generation = {
      panel.generation_=(g)
      panel.repaint
      Thread.sleep(1000)
      loop(GameOfLife.nextGeneration(g))
    }
    loop(g)
  }

}