package com.oteeninga.portal.player

import akka.actor.Actor
import com.oteeninga.portal.commons._

import scala.util.Random

abstract class Player(id: Int) extends Actor {

  override def receive: Receive = game(10, 0, 0)

  def calcDeposit(balance: Double): Double

  def game(balance: Double, lastDeposit: Double, lastLoot: Double): Receive = {
    case Deposit =>
      val deposit = calcDeposit(balance)
      sender() ! Payment(id, deposit)
      context become game(balance - deposit, deposit, lastLoot)
    case Loot(value) =>
      sender() ! Amount(id, balance + value)
      context become game(balance + value, lastDeposit, value)
  }
}

case class FreeRider(id: Int) extends Player(id) {
  override def calcDeposit(balance: Double): Double = 0
}

case class Cooperator(id: Int) extends Player(id) {
  override def calcDeposit(balance: Double): Double = balance
}

case class Casual(id: Int) extends Player(id) {
  override def calcDeposit(balance: Double): Double = Random.nextDouble() * balance
}
