package pl.edu.agh.gma.dto.gameprogress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDecisionRequest {

  private String username;
  private int roundNum;
  private double contribution;
}
