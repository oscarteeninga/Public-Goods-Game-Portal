package pl.edu.agh.gma.model.artificial


class Reinforcer(id: Int, initWallet: Double) extends ArtificialUser("Reinforcer " + id) {

  type Action = Int
  type State = Int

  val experimental_rate = 0.05
  val discount_factor = 1.00
  val step_no = 0.4
  val step_size = 4

  var q: Map[(State, Action), Double] = Map.empty
  val actions: List[Action] = (0 to 10).toList

  var lastWallet: Double = initWallet



  override def contribution(wallet: Double): Double = {
    // 0, 1, 2, ..., 10 - it represent 0%, 10%, 20%, ..., 100%
    val earnedProc = ((10 * wallet - lastWallet) / wallet).round
    lastWallet = wallet
    wallet * control(earnedProc.toInt, lastWallet)


  }

  def control(state: State, last_reward: Double): Action = {
    //TODO
    0
  }

  private def value: Double = {
    //TODO
    0
  }

  private def valueWeight: Double = {
    actions.map { action =>
      val b = epsilonGreedyPolicy(action, actions)(action)
      val p = greedyPolicy(action, actions)(action)
      p / b
    }.product
  }

  private def selectAction(actionsDistribution: Map[Action, Double]): Action = {
    val actions = actionsDistribution.keys
    val probabilities = actionsDistribution.values.toList// must be at least close
    val dsums  :Seq[Double] = probabilities.scanLeft(0.0)(_+_).tail
    val distro :Seq[Double] = dsums.init :+ 1.1 // close a possible gap
    distro.indexWhere(_ > util.Random.nextDouble())
  }

  private def epsilonGreedyPolicy(state: State, actions: List[Action]): Map[Action, Double] = {
    actions.zip(randomProbabilities(actions).zip(greedyProbabilities(state, actions)).map {
      case (random, greedy) => experimental_rate * greedy + (1 - experimental_rate) * random
    }).toMap
  }

  private def greedyPolicy(state: State, actions: List[Action]): Map[Action, Double] =
    actions.zip(greedyProbabilities(state, actions)).toMap

  private def randomProbabilities(actions: List[Action]): List[Double] =
    actions.map(_ => 1.0/actions.size)

  private def greedyProbabilities(state: State, actions: List[Action]): List[Double] = {
    val values = actions.map(action => q.getOrElse((state, action), 0))
    values.map(v => if (v == values.max) 1.0 else 0.0)
  }
}
object Reinforcer {
  def create(id: Int, initWallet: Double): Reinforcer = new Reinforcer(id, initWallet)
}
