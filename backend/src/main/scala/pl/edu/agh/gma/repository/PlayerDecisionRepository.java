package pl.edu.agh.gma.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.agh.gma.model.mongo.PlayerDecisionSummary;

public interface PlayerDecisionRepository extends MongoRepository<PlayerDecisionSummary, String> {

  //    List<PlayerDecision> findByRoundIdEquals(String roundId);
  List<PlayerDecisionSummary> findByRoundNumEquals(int round);

  //    List<PlayerDecision> findByRoundId(String roundId);
  List<PlayerDecisionSummary> findByRoundNum(int round);

}
