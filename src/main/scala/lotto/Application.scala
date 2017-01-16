package lotto

object Application {

  def Task1SystemTicketPermutations(path: String) {
    import lotto.TicketJsonProtocol._
    val systemticket: Systemschein= FileReader.readJson(path).convertTo[Systemschein]
    println(s"Combinations of systemticket")
    for(p<-systemticket.lottoCombinations) {
      println(p)
    }
  }
  
  def Task2PrintWinningClasses(path: String) {
    import lotto.TicketJsonProtocol._
    val lottery: LotteryFormat= FileReader.readJson(path).convertTo[LotteryFormat]
    val result = lottery.winningDraw.createWinningOverview(lottery.tickets: _*)
    for(line<-result) println(s"${line._1.description} : ${line._2}")
  }
}