package pl.edu.agh.gma.model.mongo;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.agh.gma.model.GameSessionToken;
import pl.edu.agh.gma.model.GameStatus;

@Getter
@Setter
@Document
@AllArgsConstructor
@ToString
public class GameDetails {

  @Id
  private String id;

  private String gameName;
  private String description;

  private GameStatus status;
  // used to generate game link
  private final GameSessionToken sessionToken;

  @DBRef
  private GameConfiguration gameConfiguration;

  private int connectedUserNum;

  private List<User> connectedUsers;

  //    @DBRef(roundId)    => nie dziala - pewnie cykliczna zaleznosc
  private int roundsNum;
  @DBRef
  private List<RoundResults> roundList = new ArrayList<>();

  public GameDetails(String gameName, String description, GameStatus status, GameSessionToken sessionToken) {
    this.gameName = gameName;
    this.description = description;
    this.status = status;
    this.sessionToken = sessionToken;
  }

  public void add(RoundResults roundResults){
    roundList.add(roundResults);
  }

}
