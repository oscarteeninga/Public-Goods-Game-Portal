package pl.edu.agh.gma.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.agh.gma.model.GameSessionToken;

@Getter
@AllArgsConstructor
@ToString
@Document
public class PlayerDecisionSummary {

  @Id
  private String id;

  private GameSessionToken sessionToken;

  private String username;

  // back reference to round
  //    private String roundId;
  private int roundNum;

  private double moneyBefore;

  private double gave;

  private double received;

  private double moneyAfter;

  public PlayerDecisionSummary(GameSessionToken sessionToken, String username, int roundNum, double moneyBefore, double gave,
      double received, double moneyAfter) {
    this.sessionToken = sessionToken;
    this.username = username;
    this.roundNum = roundNum;
    this.moneyBefore = moneyBefore;
    this.gave = gave;
    this.received = received;
    this.moneyAfter = moneyAfter;
  }
}
