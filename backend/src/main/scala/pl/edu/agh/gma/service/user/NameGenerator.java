package pl.edu.agh.gma.service.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pl.edu.agh.gma.error.exceptions.NameGeneratorException;
import pl.edu.agh.gma.model.Gender;
import pl.edu.agh.gma.model.Person;

/**
 * Parses names from #resourceName resource and stores them in memory
 * Generates a unique female or male name on command #generateRandomName()
 * Gets currently generated names in system
 */
@Slf4j
@Component
class NameGenerator {

  private final DaoUser daoUser;
  /**
   * Collection (unique) with all parsed male names from #resourceName limited by #MAX_MALE_NAMES_NUM
   */
  private final List<Person> maleExampleNames = new ArrayList<Person>();
  /**
   * Currently generated and used male names from #maleExampleNames
   */
  private final List<Person> usedMaleNames = new ArrayList<Person>();
  /**
   * Collection (unique) with all parsed female names from #resourceName limited by #MAX_FEMALE_NAMES_NUM
   */
  private final List<Person> femaleExampleNames = new ArrayList<Person>();
  /**
   * Currently generated and used female names from #femaleExampleNames
   */
  private final List<Person> usedFemaleNames = new ArrayList<Person>();
  private final Random random = new Random();
  /**
   * File with names format: (name,gender(F,M),popularity)
   * source: https://www.ssa.gov/oact/babynames/names.zip
   */
  @Value("${app.nameGenerator.source.resourceName}")
  public String resourceName;
  /**
   * Maximal amount of unique male names is 11253
   */
  @Value("${app.nameGenerator.maxNum.male}")
  public int MAX_MALE_NAMES_NUM;
  /**
   * Maximal amount of unique female names is 17360
   */
  @Value("${app.nameGenerator.maxNum.female}")
  public int MAX_FEMALE_NAMES_NUM;

  public NameGenerator(DaoUser daoUser) {
    this.daoUser = daoUser;
  }

  @PostConstruct
  private void postConstruct() {
    log.trace("Creating NameGeneratorService() and parsing resource: " + resourceName);
    parseFile();
    updateFromDb();
  }

  public void updateFromDb() {
    log.info("Retrieve information for users from database.");
    daoUser.findAll().forEach(user -> {
      Person person = new Person(user.getUsername(), new Gender(user.getGender()));

      if (person.getGender().isFemale()) {
        usedFemaleNames.add(person);
      } else if (person.getGender().isMen()) {
        usedMaleNames.add(person);
      }
    });
  }

  private void parseFile() {

    try (InputStream inputStream = new ClassPathResource(resourceName).getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader)) {

      String myLine = null;

      int male = 0, female = 0, left = 0;

      while (true) {
        try {
          if ((myLine = bufferedReader.readLine()) == null) {
            break;
          }
        } catch (IOException e) {
          log.error("Error for reading line from " + resourceName + " resource!");
          throw new NameGeneratorException("Error for reading line from " + resourceName + " resource!", e);
        }

        String[] dataRow = Objects.requireNonNull(myLine).split(",");

        Person person = new Person();
        person.setName(dataRow[0]);
        person.setGender(new Gender(dataRow[1]));

        if (dataRow[1].equals("M") && maleExampleNames.size() < MAX_MALE_NAMES_NUM && !femaleExampleNames.contains(person)
            && !maleExampleNames.contains(person)) {
          male++;
          maleExampleNames.add(person);
        } else if (dataRow[1].equals("F") && femaleExampleNames.size() < MAX_FEMALE_NAMES_NUM
            && !femaleExampleNames.contains(person) && !maleExampleNames.contains(person)) {
          female++;
          femaleExampleNames.add(person);
        } else {
          left++;
          //                System.out.println(person);
        }
      }

      log.info("#MaleNames=" + male + ", #FemaleNames= " + female + ", #LeftNames= " + left);

    } catch (IOException e) {
      log.error("Resource " + resourceName + " could not be accessed!");
      throw new NameGeneratorException("Resource " + resourceName + " could not be accessed!", e);
    }
  }

  Person generateRandomName() {
    return random.nextBoolean() ? generateRandomName(new Gender("M")) : generateRandomName(new Gender("F"));
  }

  Person generateRandomName(Gender gender) {
    if (gender.isMen()) {
      Person maleName = getMaleName();
      while (usedMaleNames.contains(maleName)) {
        maleName = getMaleName();
      }
      usedMaleNames.add(maleName);
      log.debug("Generated male name: " + maleName);
      return maleName;
    }
    if (gender.isFemale()) {
      Person femaleName = getFemaleName();
      while (usedFemaleNames.contains(femaleName)) {
        femaleName = getMaleName();
      }
      usedFemaleNames.add(femaleName);
      log.debug("Generated female name: " + femaleName);
      return femaleName;
    }

    log.warn("Gender is not defined and no name is generated!");
    return null;
  }

  private Person getMaleName() {
    return maleExampleNames.get(Math.abs(random.nextInt()) % maleExampleNames.size());
  }

  private Person getFemaleName() {
    return femaleExampleNames.get(Math.abs(random.nextInt()) % femaleExampleNames.size());
  }

  List<Person> getUsedNames(Gender gender) {
    log.debug("Used male names: " + usedMaleNames.size() + ", used female names: " + usedFemaleNames.size());

    if (gender.isMen()) {
      return usedMaleNames;
    }
    if (gender.isFemale()) {
      return usedFemaleNames;
    }
    return new ArrayList<>();
  }

  List<Person> getUsedNames() {
    log.debug("Used male names: " + usedMaleNames.size() + ", used female names: " + usedFemaleNames.size());

    List<Person> allUsedNames = new ArrayList<>();
    allUsedNames.addAll(usedMaleNames);
    allUsedNames.addAll(usedFemaleNames);
    return allUsedNames;
  }
}