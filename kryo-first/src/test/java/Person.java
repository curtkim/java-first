import java.util.Date;

class Person {
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
}
