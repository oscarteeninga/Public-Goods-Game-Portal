package pl.edu.agh.gma.service.user;

import java.util.HashMap;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;
import pl.edu.agh.gma.model.mongo.User;

@Service
@AllArgsConstructor
public class UserService {

  private final UserManager userManager;

  public Optional<User> registerUser(Person person) {
    return userManager.registerUser(person);
  }

  public Optional<User> registerRandomUserBy(Gender gender) {
    return userManager.registerRandomUser(gender);
  }

  public boolean unregisterUser(User user) {
    return userManager.unregisterUser(user);
  }

  public HashMap<String, User> getRegisteredUsers() {
    return userManager.getRegisteredUsersList();
  }

  public boolean isUserRegistered(User user) {
    return userManager.isUserRegistered(user);
  }

  public boolean isUserRegisteredById(String userId) {
    return userManager.isUserRegisteredById(userId);
  }

  public boolean isUserRegisteredByName(String username) {
    return userManager.isUserRegisteredByName(username);
  }

  public User getRegisteredUserByName(String userName) { return userManager.getRegisteredUserByName(userName); }
}
