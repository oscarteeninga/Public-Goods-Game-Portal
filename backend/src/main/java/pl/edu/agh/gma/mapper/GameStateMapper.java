package pl.edu.agh.gma.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.dto.gameprogress.GameProgressResponse;
import pl.edu.agh.gma.dto.gameprogress.GameRoundSummaryResponse;
import pl.edu.agh.gma.dto.gameprogress.GameStateResponse;
import pl.edu.agh.gma.model.GameStatus;
import pl.edu.agh.gma.model.mongo.PlayerDecisionSummary;
import pl.edu.agh.gma.model.mongo.RoundResults;
import pl.edu.agh.gma.service.util.GameState;

@Component
public class GameStateMapper {

  private Map<String, Double> extractContributions(RoundResults roundResults) {
    return roundResults
            .getPlayerRoundSummaries()
            .stream()
            .collect(Collectors.toMap(PlayerDecisionSummary::getUsername, PlayerDecisionSummary::getGave));
  }

  private double extractPayOff(RoundResults roundResults) {
    return roundResults
            .getPlayerRoundSummaries()
            .stream()
            .mapToDouble(PlayerDecisionSummary::getReceived).max().orElse(0.0);
  }

  public GameRoundSummaryResponse mapToGameRoundSummaryResponse(RoundResults roundResults) {
    return new GameRoundSummaryResponse(
        extractContributions(roundResults),
        extractPayOff(roundResults),
        roundResults.getRound()
    );
  }

  public GameStateResponse mapToGameStateResponse(GameState gameState) {
    return new GameStateResponse(
        gameState
            .getRoundListResults()
            .stream()
            .map(this::mapToGameRoundSummaryResponse)
            .collect(Collectors.toList()),
        gameState.getWallets(),
        gameState.getCurrentRoundMemory().getPlayersThatDecided(),
        gameState.getRoundsNum()
    );
  }

  public GameStateResponse getCleanGameStateResponse() {
    List<GameRoundSummaryResponse> gameRoundSummaryResponseList = new ArrayList<>();
    gameRoundSummaryResponseList.add(new GameRoundSummaryResponse(
        new HashMap<>(),
        -1.0,
        -1
    ));
    return new GameStateResponse(
        gameRoundSummaryResponseList,
        new HashMap<>(),
        new HashSet<>(),
        -1
    );
  }

  public GameProgressResponse mapToGameProgressResponse(GameState gameState){
    return new GameProgressResponse(
        gameState.getSessionToken().getStrToken(),
        gameState.getStatus(),
        gameState.getCurrentRound(),
        gameState.getRoundTimeLeft()
    );
  }

  public GameProgressResponse getCleanGameProgressResponse(){
    return new GameProgressResponse(
        "",
        GameStatus.CREATED,
        -1,
        -1L
    );
  }
}
