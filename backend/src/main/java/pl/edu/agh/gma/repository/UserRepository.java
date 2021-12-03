package pl.edu.agh.gma.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.agh.gma.model.mongo.User;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByUsername(String username);

  //    Optional<User> findByRegisteredToken(String registeredToken);

}
