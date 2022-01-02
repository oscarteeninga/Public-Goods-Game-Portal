package pl.edu.agh.gma.model.artificial;

import java.util.Random;

public class Casual extends ArtificialUser {

  public Casual(int id) {
    super("casual " + id);
  }

  @Override
  public double contribution(double wallet) {
    return wallet * new Random().nextDouble();
  }
}
