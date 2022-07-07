import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

class Person implements Serializable {
  private String name = "John Doe";
  private int age = 18;
  private Date birthDate = new Date(933191282821L);

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
// standard constructors, getters, and setters

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return age == person.age && Objects.equals(name, person.name) && Objects.equals(birthDate, person.birthDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, birthDate);
  }
}
