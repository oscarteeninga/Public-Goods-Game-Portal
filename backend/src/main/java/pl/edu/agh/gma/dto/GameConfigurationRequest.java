package pl.edu.agh.gma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfigurationRequest {

  private String gameSessionToken;

  private int roundTime;
  private int numberOfRounds;
  private double initialMoneyAmount;
  private double poolMultiplierFactor;

}