package pl.edu.agh.gma.model.artificial;

public class Cooperator extends ArtificialUser {

  public Cooperator(int id) {
    super("contributor " + id);
  }

  @Override
  public double contribution(double wallet) {
    return wallet;
  }
}
