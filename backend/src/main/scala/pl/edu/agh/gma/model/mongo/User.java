package pl.edu.agh.gma.model.mongo;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class User {

  @Id
  private String id;

  @Indexed(unique = true)
  private String username;

  private String gender;

  // use as communication token ? TODO think about it!
  //    @Indexed(unique=true)
  //    private String registeredToken;

  public User(String username, String gender) {
    this.username = username;
    this.gender = gender;
    //        this.registeredToken = registeredToken;
  }

  // with Id parameter
  public boolean strongEquals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(gender,
        user.gender);
  }

  @Override
  // without Id parameter
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(username, user.username);
  }

  @Override
  // without Id parameter
  public int hashCode() {
    return Objects.hash(username);
  }
}