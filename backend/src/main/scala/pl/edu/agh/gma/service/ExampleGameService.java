package pl.edu.agh.gma.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExampleGameService {

  //    private final GameHistory gameHistory;
  ////    private final Round currentRound;
  //
  //    private final UserRepository userRepository;  => UserPersistenceService
  //    private final RoundRepository roundRepository;
  //    private final PlayerDecisionRepository playerDecisionRepository;
  //    private final NameGeneratorService nameGeneratorService;
  //
  //    public Round saveExampleRound(){
  //        int round = Math.abs(new Random().nextInt())%100;
  //        Round _round = new Round(round, new ArrayList<>());
  //
  //        int random = Math.abs(new Random().nextInt())%4;
  //        for (int i=0; i<random; ++i) {
  //            double gave = Math.abs(new Random().nextDouble()) % 100;
  //            double received = Math.abs(new Random().nextDouble()) % 100;
  //            User user = new User(nameGeneratorService.generateRandomPerson().getName(), "F",
  //            "someToken:CA*S^DVFCXZ");
  //            user = userRepository.save(user);
  //
  //            PlayerDecision playerDecision = new PlayerDecision(
  //                     user, 1, 100.0, gave, 12, received
  //            );
  //            playerDecision = playerDecisionRepository.save(playerDecision);
  //
  //            List<PlayerDecision> playerDecisionList = _round.getPlayerDecisions();
  //            playerDecisionList.add(playerDecision);
  //            _round.setPlayerDecisions(playerDecisionList);
  //            round = Math.abs(new Random().nextInt()) % 100;
  //        }
  //
  //        _round = roundRepository.save(_round);
  //
  //        updatePlayerDecisionRoundReference(_round);
  //
  //        gameHistory.addRoundResult(_round);
  //        return _round;
  //    }
  //
  //    public void updatePlayerDecisionRoundReference(Round round){
  //        for (PlayerDecision playerDecision : round.getPlayerDecisions()){
  ////            playerDecision.setRoundId(round.getId());
  //            playerDecisionRepository.save(playerDecision);
  //        }
  //    }
  //
  //    public Round getLastRoundResult(){
  //        return gameHistory.getLastRoundResult();
  //    }
  //
  //    public List<Round> getAllHistory(){
  //        return gameHistory.getWholeGameHistory();
  //    }

  //
  //    public void configure(GameConfiguration gameConfiguration) {
  //        gameRepository.saveConfiguration(gameConfiguration);
  //    }
  //
  //
  //    // player game service information
  //    public void add(PlayerDecision playerDecision) {
  //        gameRepository.saveConfiguration(gameConfiguration);
  //    }

}