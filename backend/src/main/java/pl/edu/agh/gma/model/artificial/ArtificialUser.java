package pl.edu.agh.gma.model.artificial;

import pl.edu.agh.gma.model.mongo.User;

import java.util.Random;

public abstract class ArtificialUser extends User {

  public ArtificialUser(String name) {
    super(name, "undefined");
  }

  public abstract double contribution(double wallet);
}
