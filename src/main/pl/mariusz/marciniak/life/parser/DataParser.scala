package pl.mariusz.marciniak.life.parser

import pl.mariusz.marciniak.life.GameOfLife.Generation
import pl.mariusz.marciniak.life.Cell

object DataParser {
  val parsers : List[DataParser] = List(new CellCoordinatesDataParser, new SketchDataParser)
  
  def importData(data: String, parser: DataParser = null) : Generation = {
    if(parser == null) {
      val g = parsers.foldLeft(Set[Cell]()) {
        (result,parser) => if(result isEmpty)
          try { parser.importData(data) } catch {
            case e : ParsingException => result
            case e : IncorrectCoordinates => result 
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
	def importData(data: String): Generation = (data.split("\\n") map (line => Cell(line))).toSet
} 

class SketchDataParser extends DataParser {
	def importData(data: String): Generation = {
	  val maxElements = data.trim().length()
	  val horizontalZero =  maxElements / 2
	  (for {
	    x <- 0 to data.length()-1
	    if(data.charAt(x) != ' ')
	  } yield Cell(x - horizontalZero,0)).toSet
	}
} 