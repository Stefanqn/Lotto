package lotto
  import org.scalacheck.Gen
  import org.scalacheck.Prop.forAll
object Spielwiese extends LottoScalaCheck {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(165); 
  println("Welcome to the Scala worksheet");$skip(49); val res$0 = 

                   
  lotteryFormatGen.sample;System.out.println("""res0: Option[lotto.LotteryFormat] = """ + $show(res$0));$skip(13); 
 
val x  = 1;System.out.println("""x  : Int = """ + $show(x ))}
  
}
