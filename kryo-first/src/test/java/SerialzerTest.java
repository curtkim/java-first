import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerialzerTest {

  Kryo kryo;
  Output output;
  Input input;

  @BeforeEach
  public void init() throws FileNotFoundException {
    kryo = new Kryo();
    kryo.register(Date.class);

    output = new Output(new FileOutputStream("file.dat"));
    input = new Input(new FileInputStream("file.dat"));
  }

  @Test
  public void givenPerson_whenSerializing_thenReadCorrectly() {
    kryo.register(Person.class);
    Person person = new Person();

    kryo.writeObject(output, person);
    output.close();

    Person readPerson = kryo.readObject(input, Person.class);
    input.close();

    assertEquals(readPerson.getName(), "John Doe");
  }

  @Test
  public void givenPerson_whenUsingCustomSerializer_thenReadCorrectly() {
    Person person = new Person();
    person.setAge(0);

    kryo.register(Person.class, new PersonSerializer());
    kryo.writeObject(output, person);
    output.close();

    Person readPerson = kryo.readObject(input, Person.class);
    input.close();

    assertEquals(readPerson.getName(), "John Doe");
    assertEquals(readPerson.getAge(), 18);
  }
}
