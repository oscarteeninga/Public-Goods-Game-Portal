package pl.edu.agh.gma.model.mongo;

import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
@ToString
public class RoundResults {

  @Id
  private String id;

  private int round;

  private List<PlayerDecisionSummary> playerRoundSummaries;

  public RoundResults(int round, List<PlayerDecisionSummary> playerRoundSummaries) {
    this.round = round;
    this.playerRoundSummaries = playerRoundSummaries;
  }

  // TODO add
  //    @CreatedDate
  //    private ZonedDateTime created;

  //    @LastModifiedDate
  //    private ZonedDateTime updated;

  //
  //    public void addUserDecision(PlayerDecision playerDecision){
  //        playerDecisions.add(playerDecision);
  //    }
}
