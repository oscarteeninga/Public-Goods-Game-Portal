package pl.edu.agh.gma.service.game;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.gma.dto.gameprogress.PlayerDecisionRequest;
import pl.edu.agh.gma.service.cache.RoundMemory;

@Slf4j
public class GameEngine {

  public double calculateRoundPayoff(RoundMemory roundMemory, double poolMultiplierFactor){
    List<PlayerDecisionRequest> playerDecisionRequestList = roundMemory.getPlayerDecisionList();

    double totalContribution = playerDecisionRequestList
        .stream()
        .mapToDouble(PlayerDecisionRequest::getContribution)
        .reduce(0, Double::sum);

    int numberOfPlayers = playerDecisionRequestList.size();

    log.trace("###___ numberOfPlayers: " + numberOfPlayers + ", totalContribution: "+ totalContribution + ", poolMultiplierFactor: "+ poolMultiplierFactor);
    return (numberOfPlayers == 0) ?
        0.0 : Math.round(100*(totalContribution * poolMultiplierFactor) / (double) numberOfPlayers)/100.0;
  }
}