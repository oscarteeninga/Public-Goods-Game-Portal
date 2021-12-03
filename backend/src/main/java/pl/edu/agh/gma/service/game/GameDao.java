package pl.edu.agh.gma.service.game;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.mongo.GameDetails;
import pl.edu.agh.gma.repository.GameRepository;

@Component
@AllArgsConstructor
public class GameDao {

  private final GameRepository gameRepository;

  public GameDetails save(GameDetails gameDetails) {
    return gameRepository.save(gameDetails);
  }

  public Optional<GameDetails> findById(String id) {
    return gameRepository.findById(id);
  }

  public Optional<GameDetails> findByName(String gameName) {
    return gameRepository.findByGameName(gameName);
  }

  public void delete(GameDetails gameDetails) {
    gameRepository.delete(gameDetails);
  }

}
