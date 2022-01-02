package pl.edu.agh.gma.model.artificial

class Sensitive(id: Int, initWallet: Double) extends ArtificialUser("Sensitive " + id) {

  override def contribution(wallet: Double): Double = ???
}