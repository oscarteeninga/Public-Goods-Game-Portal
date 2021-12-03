//package pl.edu.agh.gma.service;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import pl.edu.agh.gma.model.Gender;
//import pl.edu.agh.gma.model.Person;
//import pl.edu.agh.gma.service.util.NameGenerator;
//
//import java.util.List;
//
//@Slf4j
//@Component
//@AllArgsConstructor
//public class NameGeneratorService {
//
//    private final NameGenerator nameGenerator;
//
//    public Person generateRandomPerson() {
//        return nameGenerator.generateRandomName();
//    }
//
//    public Person generateRandomPerson(Gender gender) {
//        return nameGenerator.generateRandomName(gender);
//    }
//
//    /** Get currently used names with gender [M, F] */
//    public List<Person> getUsedPeople(Gender gender) {
//        return nameGenerator.getUsedNames(gender);
//    }
//
//    /** Get all currently used names in the system */
//    public List<Person> getUsedPeople() {
//        return nameGenerator.getUsedNames();
//    }
//}