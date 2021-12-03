package pl.edu.agh.gma.controller.test;

import java.util.HashMap;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;
import pl.edu.agh.gma.model.mongo.User;
import pl.edu.agh.gma.service.user.DaoUser;
import pl.edu.agh.gma.service.user.UserService;

@RestController
@RequestMapping("test/usermanager")
@AllArgsConstructor
public class TestUserController {

  private final UserService userService;
  private final DaoUser daoUser;

  /**
   * http://localhost:8080/test/usermanager/register?username=Kasia
   * http://localhost:8080/test/usermanager/register?username=Jozek
   * http://localhost:8080/test/usermanager/get
   *
   * http://localhost:8080/test/usermanager/isregistered/id/
   * http://localhost:8080/test/usermanager/isregistered/token/
   * http://localhost:8080/test/usermanager/isregistered/name/
   */

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestParam(required = false, name = "username") String username,
      @RequestParam(required = false, name = "gender") String gender) {
    Optional<User> optUser;
    if (username != null) {
      optUser = userService.registerUser(new Person(username, new Gender(gender)));
    } else {
      Gender g = new Gender(gender);
      optUser = userService.registerRandomUserBy(g);
    }

    return optUser.map(user -> ResponseEntity.status(HttpStatus.OK).body(user))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @GetMapping("/isregistered/id/{id}")
  public ResponseEntity<Boolean> isUserRegistered(@PathVariable("id") String id) {

    return ResponseEntity.status(HttpStatus.OK).body(userService.isUserRegisteredById(id));
  }

  @GetMapping("/isregistered/name/{username}")
  public ResponseEntity<Boolean> isUserRegistered3(@PathVariable("username") String username) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.isUserRegisteredByName(username));
  }

  @GetMapping("/get")
  public ResponseEntity<HashMap<String, User>> getAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getRegisteredUsers());
  }

  @DeleteMapping("/deregister/id/{id}")
  public ResponseEntity<Boolean> deregisterUser(@PathVariable("id") String id) {

    Optional<User> optionalUser = daoUser.findById(id);
    //        user = optionalUser.orElseThrow(() -> new GameNotFoundException("nie znaleziono uzytkownika"));
    if (optionalUser.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    System.out.println("checking user: " + optionalUser.get());

    return ResponseEntity.status(HttpStatus.OK).body(userService.unregisterUser(optionalUser.get()));
  }

  //    @GetMapping("/deregister/token/{registeredToken}")
  //    public ResponseEntity<Boolean> deregisterUser2(@PathVariable("registeredToken") String registeredToken) {
  //
  //        Optional<User> optionalUser = daoUserService.findByRegisteredToken(registeredToken);
  //        if (optionalUser.isEmpty())
  //            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  //
  //        System.out.println("checking user: " + optionalUser.get());
  //
  //        return ResponseEntity
  //                .status(HttpStatus.OK)
  //                .body(userService.unregisterUser(optionalUser.get()));
  //    }

  @DeleteMapping("/deregister/name/{username}")
  public ResponseEntity<Boolean> deregisterUser3(@PathVariable("username") String username) {

    Optional<User> optionalUser = daoUser.findByUsername(username);

    System.out.println("checking user: " + optionalUser.toString());

    if (optionalUser.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return ResponseEntity.status(HttpStatus.OK).body(userService.unregisterUser(optionalUser.get()));
  }
}
