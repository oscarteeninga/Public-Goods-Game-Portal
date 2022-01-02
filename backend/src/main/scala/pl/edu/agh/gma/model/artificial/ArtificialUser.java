package pl.edu.agh.gma.model.artificial;

import pl.edu.agh.gma.model.mongo.User;

public abstract class ArtificialUser extends User {

  public ArtificialUser(String name) {
    super(name, "undefined");
  }

  public abstract double contribution(double wallet);
}
