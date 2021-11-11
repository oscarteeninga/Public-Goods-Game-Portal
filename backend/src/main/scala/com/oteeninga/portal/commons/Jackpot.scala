package com.oteeninga.portal.commons

case class Jackpot(payments: List[Payment]) {
  def all: Double = payments.map(_.value).sum

  def update(pay: Payment): Jackpot = Jackpot(pay :: payments)

  def toStats: List[(Int, Double)] = payments.map(p => (p.player, p.value))
}

object Jackpot {
  def empty: Jackpot = Jackpot(Nil)
}
