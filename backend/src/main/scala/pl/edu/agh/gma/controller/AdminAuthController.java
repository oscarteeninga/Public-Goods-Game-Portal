package pl.edu.agh.gma.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.model.AdminCredential;
import pl.edu.agh.gma.model.Bearer;
import pl.edu.agh.gma.service.AdminAuthService;

@CrossOrigin("*")
@RestController
@RequestMapping("sing/api/v1/auth")
@AllArgsConstructor
public class AdminAuthController {

  private final AdminAuthService adminAuthService;

  @PostMapping()
  public ResponseEntity<Bearer> authenticate(@RequestBody AdminCredential adminCredential) {
    return adminAuthService.authenticate(adminCredential)
        .map(bearer -> ResponseEntity.status(HttpStatus.OK).body(bearer))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
  }
}
