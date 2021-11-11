package com.oteeninga.portal

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.oteeninga.portal.commons._
import com.oteeninga.portal.util.PlotUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

case class Master() extends Actor {

  private val duration = 1 seconds
  implicit val timeout: Timeout = Timeout(duration)

  override def receive: Receive = game(Nil)

  private def getJackpot(players: List[ActorRef]): Future[Jackpot] = {
    Future.foldLeft(players.map(_ ? Deposit))(Jackpot.empty) { case (pot: Jackpot, p: Payment) => pot.update(p) }
  }

  private def sendLoot(jackpot: Jackpot, info: GameInfo): Future[Amounts] = {
    val loot = jackpot.all * info.multiplier / info.players.length
    Future.foldLeft(info.players.map(_ ? Loot(loot)))(Amounts.empty) { case (amounts: Amounts, a: Amount) => amounts.update(a) }
  }

  def game(rounds: List[Round]): Receive = {
    case Start(info) =>
      val jackpot = Await.result(getJackpot(info.players), duration)
      val amount = Await.result(sendLoot(jackpot, info), duration)
      val round = Round(jackpot, amount)
      context become game(round :: rounds)
    case Plot =>
      PlotUtil.players_amount(rounds)
      PlotUtil.player_deposit(rounds)
  }
}
