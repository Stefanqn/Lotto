package lotto



import lotto.TicketJsonProtocol._
import spray.json._

class TicketJsonProtocolSpec extends LottoSpec {
  val ls1 = Lottoschein(10, 11, 12, 13, 14)(5, 6)
  val ls2 = Lottoschein(1, 2, 3, 4, 5)(5, 6)
  val ss1 = Systemschein(1, 2, 3, 4, 5, 6, 7, 8)(5, 6, 8)
  val ss2 = Systemschein(9, 10, 11, 12, 13, 14)(1, 2, 3)
  val lottery = LotteryFormat(ls1, List(ss1, ls2))

  "Roundtrip tests" - {
    "Lottoschein round trip" in { writeReadAndTest(ls1) }
    "Lottoschein list round trip" in { writeReadAndTest(List(ls1, ls2)) }
    "Systemschein round trip" in { writeReadAndTest(ss1) }
    "Systemschein list round trip" in { writeReadAndTest(List(ss1, ss2)) }
    "Lottoschein as Ticket round trip" in { writeReadAndTest[Ticket](ls1) }
    "Systemschein as Ticket round trip" in { writeReadAndTest[Ticket](ss1) }
    "Lottoschein and Systemschein round trip" in { writeReadAndTest[List[Ticket]](List(ss1, ls1)) }
    "File format" in  {writeReadAndTest[LotteryFormat](lottery) }
  }

  def writeReadAndTest[T: JsonFormat](arg: T)(implicit writer: spray.json.JsonWriter[T]) {
    val in = arg.toJson.prettyPrint
    val back = in.parseJson.convertTo[T]
    back shouldBe arg
  }
}