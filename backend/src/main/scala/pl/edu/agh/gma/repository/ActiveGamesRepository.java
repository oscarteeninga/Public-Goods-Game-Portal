package pl.edu.agh.gma.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.agh.gma.model.mongo.FinishedGames;

public interface ActiveGamesRepository extends MongoRepository<FinishedGames, String> {

}
