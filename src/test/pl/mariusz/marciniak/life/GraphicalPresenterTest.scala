package pl.mariusz.marciniak.life

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.swing.BorderPanel

@RunWith(classOf[JUnitRunner])
class GraphicalPresenterTest extends FunSuite {

  test("should be able to create drawable cell")  {
    new DrawableCell(new Cell(0,0))
  }
  
  test("should be able to draw cell") {
    val dc = new DrawableCell(new Cell(0,0))
    assert(dc.shape(new BorderPanel) != null)
  }
}