package pl.edu.agh.gma.model.artificial;

public class FreeRider extends ArtificialUser {
  public FreeRider(int id) {
    super("freerider " + id);
  }

  @Override
  public double contribution(double wallet) {
    return 0;
  }
}
