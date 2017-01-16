package lotto
import org.scalacheck.Gen
class TicketSpec extends LottoSpec {
  "A EuroMillions instanciation" - {
    "does instanciate" in { Lottoschein(Set(1, 2, 3, 4, 5), Set(1, 2)) }
    "checks number size (1)" in { assertThrows[IllegalArgumentException] { Lottoschein(Set(1, 2, 3, 4), Set(1, 2)) } }
    "checks number size (2)" in { assertThrows[IllegalArgumentException] { Lottoschein(1, 2, 3, 4, 5, 6)(1, 2) } }
    "checks supernumber size (1)" in { assertThrows[IllegalArgumentException] { Lottoschein(Set(1, 2, 3, 4, 5, 6), Set(1)) } }
    "checks supernumber size (2)" in { assertThrows[IllegalArgumentException] { Lottoschein(Set(1, 2, 3, 4, 5, 6), Set.empty[Int]) } }
    "checks supernumber size (3)" in { assertThrows[IllegalArgumentException] { Lottoschein(Set(1, 2, 3, 4, 5, 6), Set(1, 2, 3)) } }
  }

  "Systemschein" - {
    "short ticket permutation" in {
      Systemschein(1, 2, 3, 4, 5)(1, 2, 3).lottoCombinations.toSet shouldEqual
        (Lottoschein(1, 2, 3, 4, 5)(1, 2) :: Lottoschein(1, 2, 3, 4, 5)(1, 3) :: Lottoschein(1, 2, 3, 4, 5)(2, 3) :: Nil).toSet
    }

    "system ticket permutation" in {
      systemSchein.lottoCombinations.size shouldBe binomialCoefficient(systemSchein)
    }
  }

  "Win rating" - {
    "sample1" in { Lottoschein(10, 11, 12, 13, 14)(5, 6) rateAgainst Lottoschein(1, 11, 12, 20, 21)(10, 11) shouldEqual WinningClass(2, 0) }
    "sample2" in { Lottoschein(10, 11, 12, 13, 14)(10, 6) rateAgainst Lottoschein(1, 11, 12, 20, 21)(10, 11) shouldEqual WinningClass(2, 1) }
    "sample3" in { Lottoschein(10, 11, 12, 13, 14)(10, 6) rateAgainst Lottoschein(1, 2, 3, 4, 5)(10, 11) shouldEqual None }
  }

  "test binomialCoefficient implementation" - {
    "sample 1" in { binomialCoefficient(7, 2) shouldEqual 21 }
    "sample 2" in { binomialCoefficient(7, 5) shouldEqual 21 }
    "sample 3" in { binomialCoefficient(20, 3) shouldEqual 1140 }
    "sample 4" in { binomialCoefficient(20, 20) shouldEqual 1 }
  }

  "winning class determiniation" - {
    "various tickets" in {
      val winnings = Lottoschein(10, 11, 12, 13, 14)(5, 6).createWinningOverview(
        Lottoschein(10, 11, 12, 13, 14)(5, 6),
        Lottoschein(10, 11, 12, 13, 15)(5, 6),
        Systemschein(14, 10, 13, 11, 12, 9)(1, 2, 3)).toMap
      winnings(WinningClass("Winning class 1")) shouldBe 1
      winnings(WinningClass("Winning class 3")) shouldBe 1
      winnings(WinningClass("Winning class 4")) shouldBe 1
      winnings.totalWinners shouldBe 3
    }
    "2 winners" in {
      val winningDraw = Lottoschein(10, 11, 12, 13, 14)(5, 6)
      val winnings = winningDraw.createWinningOverview(
        winningDraw,
        winningDraw).toMap
      winnings(WinningClass("Winning class 1")) shouldBe 2
      winnings.totalWinners shouldBe 2
    }
  }

  "Property based tests" - {
    import Ticket._
    import org.scalacheck.Gen._
    "lottoschein number range sanity" in {
      forAll(fixedSizeContainerGen(Lottoschein.numberSize, Gen.chooseNum(Int.MinValue, Int.MaxValue)),
        fixedSizeContainerGen(Lottoschein.superNumberSize, Gen.chooseNum(Int.MinValue, Int.MaxValue))) {
          (n: Set[Int], sn: Set[Int]) =>
            if (n.exists(e => e < minNumber || e > maxNumber) || sn.exists(e => e < minSuperNumber || e > maxSuperNumber))
              assertThrows[IllegalArgumentException](Lottoschein(n, sn))
            else Lottoschein(n, sn)
        }
    }
    "lottoschein set size sanity" in {
      forAll(containerOf[Set, Int](numberGen), containerOf[Set, Int](superNumberGen)) {
        (n: Set[Int], sn: Set[Int]) =>
          if (n.size != Lottoschein.numberSize || n.size != Lottoschein.superNumberSize)
            assertThrows[IllegalArgumentException](Lottoschein(n, sn))
          else Lottoschein(n, sn)
      }
    }
    "systemschein number range sanity" in {
      import Systemschein._
      forAll(rangeSizedContainerGen(numberMinSize, numberMaxSize, Gen.chooseNum(Int.MinValue, Int.MaxValue)),
        rangeSizedContainerGen(superNumberMinSize, superNumberMaxSize, Gen.chooseNum(Int.MinValue, Int.MaxValue))) {
          (n: Set[Int], sn: Set[Int]) =>
            if (n.exists(e => e < minNumber || e > maxNumber) || sn.exists(e => e < minSuperNumber || e > maxSuperNumber))
              assertThrows[IllegalArgumentException](Systemschein(n, sn))
            else Systemschein(n, sn)
        }
    }
    "systemschein set size sanity" in {
      forAll(containerOf[Set, Int](numberGen), containerOf[Set, Int](superNumberGen)) {
        (n: Set[Int], sn: Set[Int]) =>
          if (n.size < Systemschein.numberMinSize || n.size > Systemschein.numberMaxSize ||
            sn.size < Systemschein.superNumberMinSize || sn.size > Systemschein.superNumberMaxSize)
            assertThrows[IllegalArgumentException](Systemschein(n, sn))
          else Systemschein(n, sn)
      }
    }
    "ticket permutation" in {
      forAll { (systemSchein: Systemschein) =>
        systemSchein.lottoCombinations.size shouldBe binomialCoefficient(systemSchein)
      }
    }

    "winning class determiniation" in {
      forAll { (lotteryFormat: LotteryFormat) =>
        {
          val winnings = lotteryFormat.winningDraw.createWinningOverview(lotteryFormat.tickets: _*)
          winnings.totalWinners should be <= lotteryFormat.tickets.size
        }
      }
    }
  }

  val systemSchein = Systemschein(14, 10, 13, 11, 12, 9)(1, 2, 3)

  def binomialCoefficient(systemSchein: Systemschein): BigInt = {
    binomialCoefficient(systemSchein.numbers.size, Lottoschein.numberSize) *
      binomialCoefficient(systemSchein.superNumbers.size, Lottoschein.superNumberSize)
  }

  def binomialCoefficient(n: Int, k: Int) =
    (BigInt(n - k + 1) to n).product /
      (BigInt(1) to k).product

}