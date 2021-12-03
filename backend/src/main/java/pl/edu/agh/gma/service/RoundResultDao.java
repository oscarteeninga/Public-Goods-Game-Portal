package pl.edu.agh.gma.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.mongo.RoundResults;
import pl.edu.agh.gma.repository.RoundRepository;

@Component
@AllArgsConstructor
public class RoundResultDao {
  private final RoundRepository roundRepository;

  public List<RoundResults> findByRoundEquals(int round) {
    return roundRepository.findByRoundEquals(round);
  }

  public List<RoundResults> find(int from, int to) {
    return roundRepository.findByRoundBetween(from, to);
  }

  public List<RoundResults> findAll() {
    return roundRepository.findAll();
  }

  public RoundResults save(RoundResults r) {
    return roundRepository.save(r);
  }

  public void delete(RoundResults r) {
    roundRepository.delete(r);
  }
}
