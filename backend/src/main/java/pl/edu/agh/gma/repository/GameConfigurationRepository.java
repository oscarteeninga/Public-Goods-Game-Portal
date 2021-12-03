package pl.edu.agh.gma.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.agh.gma.model.mongo.GameConfiguration;

public interface GameConfigurationRepository extends MongoRepository<GameConfiguration, String> {

}
