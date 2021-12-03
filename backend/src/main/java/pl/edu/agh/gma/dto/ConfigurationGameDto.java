package pl.edu.agh.gma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.gma.model.GameStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationGameDto {

  private String gameSessionToken;

  private String gameName;
  private String description;

  private int roundTime;
  private int numberOfRounds;
  private double initialMoneyAmount;
  private double poolMultiplierFactor;

  private boolean isConfigured;
  private GameStatus gameStatus;
  private String gameLink;

  private int freeriders;
  private int cooperators;
  private int casuals;
}