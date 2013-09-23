package pl.mariusz.marciniak.life

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import pl.mariusz.marciniak.life.parser.DataParser
import pl.mariusz.marciniak.life.parser.CellCoordinatesDataParser
import pl.mariusz.marciniak.life.parser.SketchDataParser
import pl.mariusz.marciniak.life.parser.ParsingException

@RunWith(classOf[JUnitRunner])
class DataParserTest extends FunSuite {

  val cellCoordinatesDataParser = new CellCoordinatesDataParser
  val sketchDataParser = new SketchDataParser

  test("import many lines of coordinates") {
    val importCoordinates = """(0,0)
      (1,0)
      (21,-33)
      (22,-33)"""
    val generation = DataParser.importData(importCoordinates, cellCoordinatesDataParser)
    assert("Cell(0,0,true),Cell(1,0,true),Cell(21,-33,true),Cell(22,-33,true)" == generation.mkString(","))
  }

  test("import sketch with one element") {
    val expected = Set(Cell(0, 0))
    val sketch = "#"
    val generation = DataParser.importData(sketch, sketchDataParser)

    assert(expected.sameElements(generation))
  }

  test("import sketch with many elements in one line") {
    val expected = Set(Cell(-2, 0), Cell(-1, 0), Cell(0, 0), Cell(2, 0))
    val sketch = "### #"
    val generation = DataParser.importData(sketch, sketchDataParser)

    assert(expected.sameElements(generation))
  }

  test("import sketch with many elements in multiple lines") {
    val expected = Set(Cell(-1, -1), Cell(0, -1), Cell(1, -1), Cell(3, -1), Cell(-3, 0), Cell(-2, 0), Cell(-1, 0), Cell(2, 0), Cell(-1, 1))
    val sketch = """
  ### #
###  #
  #"""
    val generation = DataParser.importData(sketch, sketchDataParser)

    assert(expected.sameElements(generation))
  }

  test("import sketch with negligible whitespaces") {
    val expected = Set(Cell(-2, -1), Cell(2, 0), Cell(3, 0))
    val sketch = """
  #
      ##     """
    val generation = DataParser.importData(sketch, sketchDataParser)

    assert(expected.sameElements(generation))
  }

  test("import sketch with tabs") {
    intercept[ParsingException] {
      DataParser.importData("\\t###", sketchDataParser)
    }
  }

  test("import sketch with incorrect signs") {
    intercept[ParsingException] {
      DataParser.importData("&%#$", sketchDataParser)
    }
  }

}