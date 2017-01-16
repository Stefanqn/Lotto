package lotto

import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import org.scalatest.FreeSpec

abstract class LottoSpec extends FreeSpec with Matchers with PropertyChecks with LottoScalaCheck