package pl.edu.agh.gma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateNewGameResponse {

  private String gameSessionToken;

  private String gameName;
  private String description;

  private String status;
  private int connectedPlayers;
}