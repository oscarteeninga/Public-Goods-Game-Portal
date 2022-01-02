package pl.edu.agh.gma.service.game;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.mongo.GameConfiguration;
import pl.edu.agh.gma.repository.GameConfigurationRepository;

@Component
@AllArgsConstructor
public class GameConfigurationDao {

  private final GameConfigurationRepository gameConfigurationRepository;

  public GameConfiguration insert(GameConfiguration gameConfiguration) {
    return gameConfigurationRepository.insert(gameConfiguration);
  }

  public Optional<GameConfiguration> findById(String id) {
    return gameConfigurationRepository.findById(id);
  }

  public void delete(GameConfiguration gameConfiguration) {
    gameConfigurationRepository.delete(gameConfiguration);
  }

}
