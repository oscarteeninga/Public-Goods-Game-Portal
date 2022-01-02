package pl.edu.agh.gma.controller;

import java.util.HashMap;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.user.DaoUser;
import pl.edu.agh.gma.service.user.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("sing/api/v1/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final DaoUser daoUser;

  /**
   * <pre>
   * http://localhost:8080/api/user/register?username=Kasia
   * http://localhost:8080/api/user/register?username=Jozek
   * http://localhost:8080/api/user/get
   *
   * http://localhost:8080/api/user/isregistered/name/
   * </pre>
   */

  @GetMapping("/register")
  public ResponseEntity<User> registerUser(@RequestParam(required = false, name = "username") String username,
      @RequestParam(required = false, name = "gender") String gender) {
    Optional<User> optUser = Optional.ofNullable(username)
        .map(u -> userService.registerUser(new Person(u, new Gender(gender))))
        .orElseGet(() -> userService.registerRandomUserBy(new Gender(gender)));

    return optUser.map(user -> ResponseEntity.status(HttpStatus.OK).body(user))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @GetMapping("/isregistered")
  public ResponseEntity<Boolean> isUserRegistered(@RequestParam String username) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.isUserRegisteredByName(username));
  }

  @GetMapping
  public ResponseEntity<HashMap<String, User>> getAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getRegisteredUsers());
  }

  @DeleteMapping("/deregister")
  public ResponseEntity<Boolean> deregisterUser(@RequestParam String username) {

    Optional<User> optionalUser = daoUser.findByUsername(username);

    if (optionalUser.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return ResponseEntity.status(HttpStatus.OK).body(userService.unregisterUser(optionalUser.get()));
  }
}
