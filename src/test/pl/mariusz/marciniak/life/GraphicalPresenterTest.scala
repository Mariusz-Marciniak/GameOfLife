package pl.mariusz.marciniak.life

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.swing.BorderPanel

@RunWith(classOf[JUnitRunner])
class GraphicalPresenterTest extends FunSuite {

  val gp = GraphicalPresenter(new BorderPanel) 

  test("should be able to draw cell") {
    assert(gp.shape(new Cell(0,0)()) != null)
  }

  test("should be able to have different colors for alive and dead cells") {
    val cAlive = new Cell(0,0)()
    val cDead = new Cell(0,0)(false)
    assert(gp.color(cAlive) != gp.color(cDead))
  }
  
}