package pl.edu.agh.gma.dto.gameprogress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.edu.agh.gma.model.GameStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameProgressResponse {

  private String id;

  private GameStatus status;
  private Integer round;
  private Long timeLeftMs;
}
