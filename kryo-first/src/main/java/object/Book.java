package object;

public class Book {

  public int id;
  public String name;

  private Book(){
  }

  public Book(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
