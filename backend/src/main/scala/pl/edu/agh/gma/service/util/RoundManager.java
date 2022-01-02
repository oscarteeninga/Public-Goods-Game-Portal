package pl.edu.agh.gma.service.util;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.mongo.PlayerDecisionSummary;

/**
 * Stores information about players connected to each game
 */
@Component
@AllArgsConstructor
@Slf4j
public class RoundManager {

  /**
   * key = Game.sessionToken, value = List of Users for current game
   */

  // TODO THIS should be here????
  private final List<PlayerDecisionSummary> playerDecisionSummaryList;

  // TODO keep track if user already voted in this round !!!

}
