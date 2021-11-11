package com.oteeninga.portal.commons

sealed trait PlayerCommand

case object Deposit extends PlayerCommand

case class Payment(player: Int, value: Double) extends PlayerCommand

case class Amount(player: Int, amount: Double) extends PlayerCommand


sealed trait MasterCommand

case class Start(info: GameInfo) extends MasterCommand

case class Loot(amount: Double) extends MasterCommand

case class End() extends MasterCommand

case object Plot extends MasterCommand

