package pl.mariusz.marciniak.life

import scala.swing.MainFrame

import pl.mariusz.marciniak.life.GameOfLife.Generation


class GameOfLifeFrame extends MainFrame {
    val panel = new GameOfLifePanel
    def init = {
        panel.init
        contents_=(panel)
        maximize
        title = "Game of Life"
    }
    
    def startLive(g: Generation) = {
      
    }
    
    
}