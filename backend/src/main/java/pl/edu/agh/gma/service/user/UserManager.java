package pl.edu.agh.gma.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;
import pl.edu.agh.gma.model.mongo.User;

@Component
@AllArgsConstructor
@Slf4j
public class UserManager {

  private final NameGenerator nameGeneratorService;
  private final DaoUser daoUser;

  /**
   * registered users: key {name,id,token}, value: User
   */
  HashMap<String, User> registeredUsersByName;
  HashMap<String, User> registeredUsersById;
  HashMap<String, User> registeredUsersByToken;

  @PostConstruct
  public void retrieveFromDb() {
    List<User> registeredUsersList = daoUser.findAll();
    registeredUsersList.forEach(user -> registeredUsersByName.put(user.getUsername(), user));
    registeredUsersList.forEach(user -> registeredUsersById.put(user.getId(), user));
    //        registeredUsersList.forEach(user -> registeredUsersByToken.put(user.getRegisteredToken(), user));

    log.info("Retrieve information for " + registeredUsersList.size() + " users from database.");
    //        registeredUsersNames = registeredUsersList.stream()
    //                .map(User::getUsername)
    //                .collect(Collectors.toList());
  }

  public Optional<User> registerUser(Person person) {
    User _user = new User();
    if (person.getName() != null) {
      _user.setUsername(person.getName());
      _user.setGender(person.getGender().isValid() ? person.getGender().toString()
          : nameGeneratorService.generateRandomName().getGender().toString());
      return Optional.ofNullable(saveUser(_user));
    }
    return Optional.empty();
  }

  /**
   * return user with id reference in database
   */
  private User saveUser(User user) {
    log.debug("Starting to register new user: " + user);

    if (!isUserRegistered(user)) {

      //            if (registeredUsersNames.contains(user.getUsername()))
      //                return null;

      // just in case there was a problem with saving to database
      registeredUsersByName.put(user.getUsername(), user);
      //            registeredUsersByToken.put(user.getRegisteredToken(), user);
      User _user = daoUser.insert(user);

      addUser(_user);
      //            registeredUsersByUser.put(_user, token);
      //            registeredUsersNames.add(_user.getUsername());

      log.info("Registered new user: " + _user);
      return _user;
    }
    log.info("Coudn't register user: " + user);
    return null;
  }

  public boolean isUserRegistered(User user) {
    log.trace("Checking if user registered: " + user);

    return isUserRegisteredByName(user.getUsername());
  }

  public boolean isUserRegisteredByName(String username) {
    log.trace("Checking if user registered by Name: " + username);

    return registeredUsersByName.containsKey(username);
  }

  private void addUser(User _user) {
    registeredUsersByName.put(_user.getUsername(), _user);
    registeredUsersById.put(_user.getId(), _user);
    //        registeredUsersByToken.put(_user.getRegisteredToken(), _user);
  }

  public Optional<User> registerRandomUser(Gender gender) {
    User _user = new User();
    Person person;
    if (gender.isValid()) {
      person = nameGeneratorService.generateRandomName(gender);
    } else {
      person = nameGeneratorService.generateRandomName();
    }

    _user.setUsername(person.getName());
    _user.setGender(person.getGender().toString());
    log.debug("Generated random user: " + _user);
    return Optional.ofNullable(saveUser(_user));
  }

  public boolean isUserRegisteredById(String userId) {
    log.trace("Checking if user registered by Id: " + userId);

    return registeredUsersById.containsKey(userId);
  }

  public boolean isUserRegisteredByToken(String userToken) {
    log.trace("Checking if user registered by token: " + userToken);

    return registeredUsersByToken.containsKey(userToken);
  }

  public boolean unregisterUser(User user) {
    log.trace("Unregistering user: " + user);

    if (isUserRegistered(user)) {
      removeUser(user);

      Optional<User> _userOpt = daoUser.findById(user.getId());

      if (_userOpt.isEmpty()) {
        log.warn("User was not defined in database: " + user);
        return false;
      }

      User _user = _userOpt.get();
      // overwrite user token
      daoUser.delete(_user);

      log.info("Unregistered user: " + _user);
    }

    return false;
  }

  private void removeUser(User user) {
    registeredUsersByName.remove(user.getUsername());
    registeredUsersById.remove(user.getId());
    //        registeredUsersByToken.remove(user.getRegisteredToken());
  }

  public HashMap<String, User> getRegisteredUsersList() {
    return registeredUsersByName;
  }

  public User getRegisteredUserById(String userId) {
    return registeredUsersById.get(userId);
  }

  public User getRegisteredUserByName(String userName) {
    return registeredUsersByName.get(userName);
  }

}
