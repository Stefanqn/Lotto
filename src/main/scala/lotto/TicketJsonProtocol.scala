package lotto

import spray.json._
import spray.json.JsonFormat

object TicketJsonProtocol extends DefaultJsonProtocol {
  val lottoscheinSimpleFormat = jsonFormat2(Lottoschein.apply)
  val systemscheinSimpleFormat = jsonFormat2(Systemschein.apply)

  private val lottoschein = "Lottoschein"
  private val systemschein = "Systemschein"

  implicit val lottoscheinWrappedFormat = new WrappedFormat(lottoschein, lottoscheinSimpleFormat)
  implicit val systemscheinWrappedFormat = new WrappedFormat(systemschein, systemscheinSimpleFormat)

  implicit object ticketFormat extends RootJsonFormat[Ticket] {
    def write(ticket: Ticket) = ticket match {
      case ls: Lottoschein  => lottoscheinWrappedFormat.write(ls)
      case ss: Systemschein => systemscheinWrappedFormat.write(ss)
    }

    def read(value: JsValue) = value.asJsObject match {
      case JsObject(map) if map.contains(lottoschein) => lottoscheinWrappedFormat.read(value)
      case JsObject(map) if map.contains(systemschein) => systemscheinWrappedFormat.read(value)
      case _ => deserializationError("ticket type expected")
    }
  }

  class WrappedFormat[T](val field: String, format: JsonFormat[T]) extends RootJsonFormat[T] {
    def write(obj: T) = JsObject(field -> format.write(obj))
    def read(value: JsValue): T = format.read(value.asJsObject.fields(field))
  }

  implicit val lotteryFormat: RootJsonFormat[LotteryFormat] = jsonFormat2(LotteryFormat.apply)
}

case class LotteryFormat(val winningDraw: Lottoschein, val tickets: List[Ticket]) 