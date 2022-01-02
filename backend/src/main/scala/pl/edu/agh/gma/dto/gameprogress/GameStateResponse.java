package pl.edu.agh.gma.dto.gameprogress;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameStateResponse {

  private List<GameRoundSummaryResponse> gameHistory;
  private Map<String, Double> walletsState;
  private Set<String> playersThatDecided;
  private int currentRound;

}
