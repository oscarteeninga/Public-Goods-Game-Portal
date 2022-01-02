package pl.edu.agh.gma.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.GameStatus;
import pl.edu.agh.gma.model.mongo.GameDetails;

public interface GameRepository extends MongoRepository<GameDetails, String> {

  List<GameDetails> findByGameNameContaining(String gameName);

  Optional<GameDetails> findBySessionToken(GameSessionToken sessionToken);

  Optional<GameDetails> findByGameName(String gameName);


  List<GameDetails> findByStatus(GameStatus status);

}
