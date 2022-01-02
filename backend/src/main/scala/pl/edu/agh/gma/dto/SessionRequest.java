package pl.edu.agh.gma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequest {

  private String username;
  private String gameSessionToken;

}
