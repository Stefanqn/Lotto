package lotto

object WinningClass {
  val rules = init(
    ("Winning class 1 ", 5, 2),
    ("Winning class 2 ", 5, 1),
    ("Winning class 3 ", 5, 0),
    ("Winning class 4 ", 4, 2),
    ("Winning class 5 ", 4, 1),
    ("Winning class 6 ", 4, 0),
    ("Winning class 7 ", 3, 2),
    ("Winning class 8 ", 2, 2),
    ("Winning class 9 ", 3, 1),
    ("Winning class 10", 3, 0),
    ("Winning class 11", 1, 2),
    ("Winning class 12", 2, 1),
    ("Winning class 13", 2, 0))

  private val ratingMap: Map[(Int, Int), WinningClass] = ratingMap(rules)
  private val nameMap: Map[String, WinningClass] = rules.map(r => (r.description, r)).toMap

  def apply(name: String) = nameMap(name)
  def apply(nums: Int, superNums: Int) = ratingMap.get((nums, superNums))

  private def ratingMap(args: List[WinningClass]) =
    Map(args.map { case wc @ WinningClass(_, num, superNum) => (num, superNum) -> wc }: _*)

  private def init(args: (String, Int, Int)*) = {
    args.map { case (desc, nums, superNums) => WinningClass(desc.trim, nums, superNums) }.toList
  }

  implicit class totalWinnersFromSeqList(args: List[(WinningClass, Seq[Ticket])]) {
    def totalWinners: Int = (0 /: args.map(_._2))(_ + _.size)
  }
  implicit class totalWinnersFromIntList(args: List[(WinningClass, Int)]) {
    def totalWinners: Int = (0 /: args.map(_._2))(_ + _)
  }
  implicit class totalWinnersFromIntMap(args: Map[WinningClass, Int]) {
    def totalWinners: Int = (0 /: args.values)(_ + _)
  }
  implicit class totalWinnersFromSeqMap(args: Map[WinningClass, Seq[Ticket]]) {
    def totalWinners: Int = (0 /: args.values)(_ + _.size)
  }
}

case class WinningClass(val description: String, val numberMatches: Int, val superNumberMatches: Int)