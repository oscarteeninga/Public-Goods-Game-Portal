package com.oteeninga.portal.util

import com.oteeninga.portal.commons.Round
import plotly.Plotly._
import plotly._

object PlotUtil {

  private def seriesPerPlayer(rounds: List[Round]): List[(Int, List[(Int, Double, Double)])] = {
    rounds
      .reverse
      .zipWithIndex
      .flatMap { case (r, idx) => r.toStats.map(s => (s._1, (idx, s._2, s._3))) }
      .groupBy(_._1)
      .view
      .mapValues(data => data.map(d => d._2)).toList.sortBy(_._1)
  }

  def players_amount(rounds: List[Round]): Unit = {
    seriesPerPlayer(rounds).map {
      case (id, data) =>
        val amount = data.map(d => (d._1, d._3)).sortBy(_._1).unzip
        Scatter().withX(amount._1).withY(amount._2).withName("Player " + id)
    }.plot(
      title = "Players amount"
    )
  }

  def player_deposit(rounds: List[Round]): Unit = {
    seriesPerPlayer(rounds).map {
      case (id, data) =>
        val pay = data.map(d => (d._1, d._2)).sortBy(_._1).unzip
        Scatter().withX(pay._1).withY(pay._2).withName("Player " + id)
    }.plot(
      title = "Players deposit"
    )
  }
}
