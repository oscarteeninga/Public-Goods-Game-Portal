package pl.edu.agh.gma.service;

import java.util.Map;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * used to generate new tokens that are unique in a given map collection
 **/
@Service
@NoArgsConstructor
public class TokenService {

  @Value("${app.token.maxTries}")
  public int maxTries;

  public String generateNewTokenFor(Map map) {
    return generateNewTokenFor("", map);
  }

  public String generateNewTokenFor(String prefix, Map map) {
    String gameToken;
    int tries = 0;

    do {
      if (prefix.length() > 0) {
        gameToken = prefix + "-" + UUID.randomUUID().toString().toUpperCase();
      } else {
        gameToken = UUID.randomUUID().toString().toUpperCase();
      }
      tries++;
    } while (isTokenTakenIn(gameToken, map) && tries < maxTries);

    return gameToken;
  }

  private boolean isTokenTakenIn(String tokenKey, Map map) {
    return map.containsKey(tokenKey);
  }

}
