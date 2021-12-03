package pl.edu.agh.gma.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.gma.model.AdminCredential;
import pl.edu.agh.gma.model.Bearer;

@Service
@AllArgsConstructor
// TODO #14 This will be deleted in the future
public class AdminAuthService {

  private final AdminAuthManager adminAuthManager;

  public Optional<Bearer> authenticate(AdminCredential adminCredential) {
    return adminAuthManager.checkCredentials(adminCredential) ?
        Optional.ofNullable(adminAuthManager.generateBearer()) : Optional.empty();
  }

  public boolean isBearerInvalid(String bearerToken){
    return !adminAuthManager.isBearerValid(bearerToken);
  }
}