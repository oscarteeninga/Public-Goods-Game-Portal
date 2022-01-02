package pl.edu.agh.gma.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.dto.GameInfoResponse;
import pl.edu.agh.gma.service.util.GameState;

@Component
public class GameInfoMapper {

  @Value("${app.url.frontendBase}")
  private String frontendUrl;

  public GameInfoResponse mapToGameInfoResponse(GameState gameState) {
    return new GameInfoResponse(gameState.getGameID(),
        gameState.getSessionToken().getStrToken(), gameState.getGameName(),
        gameState.getDescription(), gameState.getStatus(),
        gameState.getConnectedUserNum(),
        frontendUrl + "/user/game/" + gameState.getSessionToken().getStrToken());
  }

}
