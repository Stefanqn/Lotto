package lotto

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary
trait LottoScalaCheck {
  import Ticket._

  val numberGen = choose(minNumber, maxNumber)
  val superNumberGen = choose(minSuperNumber, maxSuperNumber)

  def fixedSizeContainerGen[T](size: Int, gen: Gen[T]) = containerOfN[Set, T](size, gen) suchThat { _.size == size }
  def rangeSizedContainerGen[T](minSize: Int, maxSize: Int, gen: Gen[T]) = for (
    ni <- choose(minSize, maxSize);
    c <- containerOfN[Set, T](ni, gen) suchThat { _.size >= minSize }
  ) yield c

  val lottoscheinGen = {
    for (
      n <- fixedSizeContainerGen(Lottoschein.numberSize, numberGen);
      s <- fixedSizeContainerGen(Lottoschein.superNumberSize, superNumberGen)
    ) yield Lottoschein(n, s)
  }
  implicit val arbLottoschein = Arbitrary(lottoscheinGen)

  val systemscheinGen = {
    import Systemschein._
    for (
      n <- rangeSizedContainerGen(numberMinSize, numberMaxSize, numberGen);
      s <- rangeSizedContainerGen(superNumberMinSize, superNumberMaxSize, superNumberGen)
    ) yield Systemschein(n, s)
  }
  implicit val arbSystemschein = Arbitrary(systemscheinGen)

  val ticketGen: Gen[Ticket] = oneOf(lottoscheinGen, systemscheinGen)
  implicit val arbTicket = Arbitrary(ticketGen)

  val lotteryFormatGen: Gen[LotteryFormat] = for {
    winner <- lottoscheinGen;
    tickets <- ticketListGen
  } yield LotteryFormat(winner, tickets)
  implicit val arbLotteryFormat = Arbitrary(lotteryFormatGen)

  // List generators 
  val lottoListGen = // circumvent failing lottoscheinGen (suchThat)
    for (
      max <- choose(1, 500);
      i <- (1 to max).toList;
      e <- lottoscheinGen.sample
    ) yield e

  implicit val arbLottoList = Arbitrary(lottoListGen)

  val systemscheinListGen =
    for (
      max <- choose(1, 500);
      i <- (1 to max).toList;
      e <- systemscheinGen.sample
    ) yield e

  implicit val arbSystemscheinList = Arbitrary(systemscheinListGen)

  val ticketListGen =
    for (
      max <- choose(1, 500);
      i <- (1 to max).toList;
      e <- ticketGen.sample
    ) yield e

  implicit val arbTicketList = Arbitrary(ticketListGen)
  // List generators ()
}

/** Generator sanity checks */
object LottoScalaCheckProperties extends Properties("LottoScalaCheck") with LottoScalaCheck {
  property("Lottoschein") = forAll(lottoscheinGen) {
    l: Lottoschein =>
      l.numbers.size == Lottoschein.numberSize
  }
}

