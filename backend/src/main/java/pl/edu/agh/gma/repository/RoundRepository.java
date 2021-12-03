package pl.edu.agh.gma.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.agh.gma.model.mongo.RoundResults;

public interface RoundRepository extends MongoRepository<RoundResults, String> {

  List<RoundResults> findByRoundEquals(int round);

  List<RoundResults> findByRoundBetween(int from, int to);

}
