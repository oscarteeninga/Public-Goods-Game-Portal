package pl.edu.agh.gma.service.user;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.repository.UserRepository;

@Component
@AllArgsConstructor
public class DaoUser {

  private final UserRepository userRepository;

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User insert(User user) {
    return userRepository.insert(user);
  }

  public void delete(User user) {
    userRepository.delete(user);
  }

}
