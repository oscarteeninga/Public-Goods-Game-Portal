package pl.edu.agh.gma.service;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.AdminCredential;
import pl.edu.agh.gma.model.Bearer;

@Component
// TODO #14 This will be deleted in the future
public class AdminAuthManager {

  // from Bearer token to actual Bearer
  private final HashMap<String, Bearer> activeBearers = new HashMap<>();
  private final TokenService tokenService;

  @Value("${app.admin.login}")
  private String adminLogin;

  @Value("${app.admin.password}")
  private String adminPassword;

  @Value("${app.admin.bearer.timeout.ms}")
  private Long defaultTimeoutMS;

  private AdminAuthManager(TokenService tokenService) {
    this.tokenService = tokenService;
  }


  /**
   * Generate and save new bearer token valid for defaultTimeoutMS
   *
   * @return bearer token
   */
  public Bearer generateBearer() {
    Bearer newBearer =
        new Bearer(tokenService.generateNewTokenFor("BEARER", activeBearers), System.currentTimeMillis());
    activeBearers.put(newBearer.getToken(), newBearer);
    return newBearer;
  }

  /**
   * @param adminCredential - Check if passed credentials are valid (username, password)
   *
   * @return true if match defined values
   */
  public boolean checkCredentials(AdminCredential adminCredential) {
    return adminCredential.getUsername().equals(adminLogin) && adminCredential.getPassword().equals(adminPassword);
  }

  /**
   * Check if bearer is still valid:
   * - if match one of active bearer
   * - if created no more than defaultTimeoutMS ago
   *
   * @param bearerToken token from Bearer
   *
   * @return true if valid
   */
  public boolean isBearerValid(String bearerToken) {
    if (activeBearers.containsKey(bearerToken)) {
      Bearer bearer = activeBearers.get(bearerToken);
      if (System.currentTimeMillis() - bearer.getCreationTimestamp() <= defaultTimeoutMS) {
        return true;
      } else {
        activeBearers.remove(bearerToken);
      }
    }
    return false;
  }
}
