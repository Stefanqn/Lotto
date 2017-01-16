package lotto

import scala.io.Source
import spray.json._
import spray.json.JsonFormat

object FileReader {
  def readJson(filePath: String):JsValue =  Source.fromFile(filePath).mkString.parseJson
}