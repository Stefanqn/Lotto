package lotto

import java.io.File

import scala.io.Source

class TicketReaderSpec extends LottoSpec {
  import lotto.TicketJsonProtocol._
  import spray.json._

  "read system ticket" - {
    "by relative path" in {
      val result = retrieveSourceByFile("systemticket1.json").mkString.parseJson.convertTo[Systemschein]
      result shouldBe systemscheinSample
    }

    "by absolute path" in {
      val absolutePath = retrieveAbsoluteFilePath("systemticket1.json")
      val result = FileReader.readJson(absolutePath.toString).convertTo[Systemschein]
      result shouldBe systemscheinSample
    }

/*    "print summary" in {
      val absolutePath = retrieveAbsoluteFilePath("lottery.json")
      Application.Task2PrintWinningClasses(absolutePath.toString)
    }*/

  }

  val systemscheinSample = Systemschein(14, 10, 13, 11, 12, 15)(1, 2, 3)
  def retrieveSourceByFile(filename: String) = Source.fromFile(s"src/test/resources/sampleData/$filename")
  def retrieveAbsoluteFilePath(filename: String) = new File(s"src/test/resources/sampleData/$filename").toPath.toAbsolutePath

}