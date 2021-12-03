package pl.edu.agh.gma.service.cache;

import java.util.ArrayList;
import java.util.List;
import pl.edu.agh.gma.model.mongo.RoundResults;

public class GameHistory {

  private final List<RoundResults> gameHistory;

  public GameHistory() {
    this.gameHistory = new ArrayList<>();
  }

  public void addRoundResult(RoundResults roundResult) {
    gameHistory.add(roundResult);
  }

  public RoundResults getLastRoundResult() {
    return gameHistory.get(gameHistory.size() - 1);
  }

  public List<RoundResults> getWholeGameHistory() {
    return gameHistory;
  }

}
