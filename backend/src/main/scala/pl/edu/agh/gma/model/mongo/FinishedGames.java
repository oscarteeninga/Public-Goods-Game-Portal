package pl.edu.agh.gma.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class FinishedGames {

  @Id
  private String id;

  @DBRef
  private GameDetails finishedGameDetails;

  public FinishedGames(GameDetails finishedGameDetails) {
    this.finishedGameDetails = finishedGameDetails;
  }
}