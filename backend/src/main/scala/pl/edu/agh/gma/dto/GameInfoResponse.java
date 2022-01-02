package pl.edu.agh.gma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.gma.model.GameStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInfoResponse {

  private String id;
  private String gameSessionToken;

  private String gameName;
  private String description;

  private GameStatus status;
  private Integer connectedPlayers;

  private String linkForPlayers;
}
