package com.oteeninga.portal.commons

case class Round(jackpot: Jackpot, amounts: Amounts) {
  def toStats: List[(Int, Double, Double)] = {
    val j = jackpot.toStats.toMap
    val a = amounts.toStats.toMap
    j.keys.toList.map { key => (key, j(key), a(key)) }
  }
}
