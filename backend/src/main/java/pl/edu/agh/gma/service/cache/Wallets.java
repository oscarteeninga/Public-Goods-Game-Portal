package pl.edu.agh.gma.service.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.edu.agh.gma.model.mongo.User;

public class Wallets {

  // username -> wallet   state for game
  private final Map<String, Double> walletState;

  public Wallets() {
    this.walletState = new HashMap<>();
  }

  public void initializeWalletStates(double initialUserMoney, List<User> connectedUsers){
    connectedUsers.forEach(user -> updateWalletState(user.getUsername(), initialUserMoney));
  }


  public boolean haveEnoughMoney(String username, double moneyToTake){
    return (walletState.get(username) >= moneyToTake);
  }

  public double takeMoneyFrom(String username, double moneyToTake) {

    double currentMoneyAmount = walletState.get(username);

    updateWalletState(username, currentMoneyAmount - moneyToTake);
    return moneyToTake;
  }

  public boolean giveMoneyTo(String username, double moneyToGive) {

    double currentMoneyAmount = walletState.get(username);

    updateWalletState(username, Math.round(100*(currentMoneyAmount + moneyToGive))/100.0);
    return true;
  }


  private void updateWalletState(String username, double newMoneyAmount){
    walletState.put(username, newMoneyAmount);
  }

  public Map<String, Double> getAllUserWallets() {
    return walletState;
  }

  public double getCurrentMoney(String username) {
    return walletState.get(username);
  }

  @Override
  public String toString() {
    return "Wallets{" + "walletState=" + walletState + '}';
  }
}
