package pl.edu.agh.gma.dto.gameprogress;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameRoundSummaryResponse {

  private Map<String, Double> contributions;
  private double payoff;
  private int round;

}
