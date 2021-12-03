package pl.edu.agh.gma.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class GameConfiguration {

  @Id
  private String id;

  private int roundTime;
  private int numberOfRounds;
  private double initialMoneyAmount;
  private double poolMultiplierFactor;

  public GameConfiguration(int roundTime, int numberOfRounds, double initialMoneyAmount, double poolMultiplierFactor) {
    this.roundTime = roundTime;
    this.numberOfRounds = numberOfRounds;
    this.initialMoneyAmount = initialMoneyAmount;
    this.poolMultiplierFactor = poolMultiplierFactor;
  }
}
