package pl.edu.agh.gma.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bearer {

  String token;
  Long creationTimestamp;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Bearer bearer = (Bearer) o;
    return Objects.equals(token, bearer.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }
}
