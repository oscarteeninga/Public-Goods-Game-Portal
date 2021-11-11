package com.oteeninga.portal.commons

case class Amounts(amounts: List[Amount]) {
  def update(a: Amount): Amounts = Amounts(a :: amounts)

  def toStats: List[(Int, Double)] = {
    amounts.map(a => (a.player, a.amount))
  }
}

case object Amounts {
  def empty: Amounts = Amounts(Nil)
}
