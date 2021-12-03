package pl.edu.agh.gma.model;

public class Gender {

  String gender;

  public Gender(String gender) {
    this.gender = gender;
  }

  public boolean notNull() {
    return !isNull();
  }

  public boolean isNull() {
    return this.gender == null;
  }

  public boolean notValid() {
    return !isValid();
  }

  public boolean isValid() {
    return notNull() && (isMen() || isFemale());
  }

  public boolean isMen() {
    return notNull() && (this.gender.equalsIgnoreCase("M") || this.gender.equalsIgnoreCase("MALE"));
  }

  public boolean isFemale() {
    return notNull() && (this.gender.equalsIgnoreCase("F") || this.gender.equalsIgnoreCase("FEMALE"));
  }

  @Override
  public String toString() {
    return getGender();
  }

  public String getGender() {
    return gender;
  }
}
