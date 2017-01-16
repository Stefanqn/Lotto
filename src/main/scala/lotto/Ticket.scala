package lotto

object Ticket {
  val minNumber, minSuperNumber = 1
  val maxNumber = 50
  val maxSuperNumber = 12
}

sealed trait Ticket {
  import Ticket._
  def numbers: Set[Int]
  def superNumbers: Set[Int]

  require(numbers.forall(n => n >= minNumber && n <= maxNumber), "numbers not in range")
  require(superNumbers.forall(n => n >= minSuperNumber && n <= maxSuperNumber), "supernumbers not in range")

  def rateAgainst(winningDraw: Lottoschein): Option[WinningClass] = 
    WinningClass(numbers.intersect(winningDraw.numbers).size, superNumbers.intersect(winningDraw.superNumbers).size)
}

case class Lottoschein(numbers: Set[Int], superNumbers: Set[Int]) extends Ticket {
  require(Lottoschein.numberSize == numbers.size, s"wrong number size ${numbers.size} of $this")
  require(Lottoschein.superNumberSize == superNumbers.size, s"wrong supernumber size ${superNumbers.size} of $this")

  def rate(tickets: Ticket*): Map[WinningClass, Seq[Ticket]] = {
    val winners = for (
      t <- tickets;
      wc <- t.rateAgainst(this)
    ) yield (t, wc)
    val winnersMultiMap = winners.groupBy { case (_, wc) => wc }.mapValues(_.map(_._1))
    winnersMultiMap
  }

  def createWinningOverview(participatingTickets: Ticket*) = {
    val winners = rate(participatingTickets: _*)
    for (
      wc <- WinningClass.rules;
      size = winners.get(wc).map(_.size).getOrElse(0)
    ) yield wc -> size
  }
}

object Lottoschein {
  val numberSize = 5
  val superNumberSize = 2

  def apply(numbers: Int*)(numbers2: Int*): Lottoschein = Lottoschein(numbers.toSet, numbers2.toSet)
}

case class Systemschein(numbers: Set[Int], superNumbers: Set[Int]) extends Ticket {
  import Systemschein._
  require(numbers.size >= numberMinSize && numbers.size <= numberMaxSize, s"wrong number size ${numbers.size} of $this")
  require(superNumbers.size >= superNumberMinSize && superNumbers.size <= superNumberMaxSize, s"wrong supernumber size ${superNumbers.size} of $this") 

  def lottoCombinations = {
    val superNumberCombination = superNumbers.toList.combinations(2).toList
    (for (
      nums <- numbers.toList.combinations(5);
      superNums <- superNumberCombination
    ) yield Lottoschein(nums: _*)(superNums: _*)).toSet
  }
}

object Systemschein {
  val numberMinSize = 5
  val numberMaxSize = 10
  val superNumberMinSize = 3
  val superNumberMaxSize = 5
  def apply(numbers: Int*)(numbers2: Int*): Systemschein = Systemschein(numbers.toSet, numbers2.toSet)

}

