import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import object.Person;
import org.junit.jupiter.api.Assertions;
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
    System.out.println("output.position()= "+output.position());              // 18 bytes
    output.close();

    Person readPerson = kryo.readObject(input, Person.class);
    input.close();

    assertEquals(readPerson.getName(), "John Doe");
  }

  @Test
  public void givenPerson_whenUsingCustomSerializer_thenReadCorrectly() {
    Person person = new Person("K", new Date());

    kryo.register(Person.class, new PersonSerializer());
    kryo.writeObject(output, person);
    System.out.println("output.position()= "+output.position());              // 10 bytes
    output.close();

    Person readPerson = kryo.readObject(input, Person.class);
    input.close();

    assertEquals(readPerson.getName(), "K");
    assertEquals(readPerson.getAge(), 18);
  }

  @Test
  public void copy(){
    Person person = new Person("K", new Date());
    kryo.register(Person.class, new PersonSerializer());

    // PersonSerializer가 copy를 구현하지 않았다.
    Assertions.assertThrows(KryoException.class, () -> {
      kryo.copy(person);
    });
  }
}
