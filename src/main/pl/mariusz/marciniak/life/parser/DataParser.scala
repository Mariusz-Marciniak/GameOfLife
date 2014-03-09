package pl.mariusz.marciniak.life.parser

import pl.mariusz.marciniak.life.GameOfLife.Generation
import pl.mariusz.marciniak.life.Cell

object DataParser {
  val parsers: List[DataParser] = List(new CellCoordinatesDataParser, new SketchDataParser)

  def importData(data: String, parser: DataParser = null): Generation = {
    if (parser == null) {
      val g = parsers.foldLeft(Set[Cell]()) {
        (result, parser) =>
          if (result isEmpty)
            try { parser.importData(data) } catch {
              case e: ParsingException => result
              case e: IncorrectCoordinates => result
            }
          else result
      }
      if (g isEmpty) {
        throw new ParsingException("Not found proper parser")
      } else g
    } else parser.importData(data)

  }
}

trait DataParser {
  def importData(data: String): Generation
}

class CellCoordinatesDataParser extends DataParser {
  def importData(data: String): Generation = (data.split("\\r?\\n") map (line => Cell(line))).toSet
}

class SketchDataParser extends DataParser {

  def importData(data: String): Generation = {
    if (validate(data))
      importData(data.split("\\r?\\n").toList.filter(_.trim.length != 0))
    else 
      throw new ParsingException("Imported data has incorrect format")
  }

  private def validate(data: String): Boolean = {
    data.replaceAll("[ #\\n\\r]*", "").isEmpty
  }

  private def importData(lines: List[String]): Generation = {
    val firstNotSpaceInLine = lines.map(l => l.indexWhere(_ != ' ')).min
    val lastNotSpaceInLine = lines.map(l => l.length - l.reverse.indexWhere(_ != ' ')).max
    val width = lastNotSpaceInLine - firstNotSpaceInLine
    val horizontalZero = (width + firstNotSpaceInLine) / 2
    val verticalZero = lines.size / 2

    def importHeadLine(leftLines: List[String], y: Int, agg: Set[Cell]): Set[Cell] = {
      if (leftLines.isEmpty)
        agg
      else {
        val l = leftLines.head
        val cells = (for {
          x <- firstNotSpaceInLine until Math.min(lastNotSpaceInLine, l.length)
          if (l.charAt(x) != ' ')
        } yield Cell(x - horizontalZero, y - verticalZero)()).toSet
        importHeadLine(leftLines.tail, y + 1, agg ++ cells)
      }
    }

    importHeadLine(lines, 0, Set[Cell]())

  }
} 