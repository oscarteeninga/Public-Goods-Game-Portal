package pl.edu.agh.gma.service.user;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;

@Slf4j
@Component
@AllArgsConstructor
// public for testing by API,   NOTE: USE NameGenerator instead
public class TestNameGenerator {

  private final NameGenerator nameGenerator;

  public Person generateRandomName() {
    return nameGenerator.generateRandomName();
  }

  public Person generateRandomName(Gender gender) {
    return nameGenerator.generateRandomName(gender);
  }

  /**
   * Get currently used names with gender [M, F]
   */
  public List<Person> getUsedNames(Gender gender) {
    return nameGenerator.getUsedNames(gender);
  }

  /**
   * Get all currently used names in the system
   */
  public List<Person> getUsedNames() {
    return nameGenerator.getUsedNames();
  }
}
