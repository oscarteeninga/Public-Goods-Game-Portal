package pl.edu.agh.gma.controller.test;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;
import pl.edu.agh.gma.service.user.TestNameGenerator;

@RestController
@RequestMapping("test/namegenerator")
@AllArgsConstructor
public class TestNameGeneratorController {

  private final TestNameGenerator nameGeneratorService;

  @GetMapping
  public ResponseEntity<Person> getExampleName(@RequestParam(required = false, name = "gender") String genderStr) {
    Gender gender = new Gender(genderStr);

    if (gender.notValid() && gender.notNull()) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    Person exampleNmae =
        gender.isValid() ? nameGeneratorService.generateRandomName(gender) : nameGeneratorService.generateRandomName();

    return ResponseEntity.status(HttpStatus.OK).body(exampleNmae);
  }

  @GetMapping("/get")
  public ResponseEntity<List<Person>> getUsedNames(@RequestParam(required = false, name = "gender") String genderStr) {
    Gender gender = new Gender(genderStr);

    if (gender.notValid() && gender.notNull()) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    List<Person> usedNames =
        gender.isValid() ? nameGeneratorService.getUsedNames(gender) : nameGeneratorService.getUsedNames();

    return ResponseEntity.status(HttpStatus.OK).body(usedNames);
  }
}
