import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundTripTest {

  @Test
  public void testDate(){
    Kryo kryo = new Kryo();

    kryo.register(Date.class);

    Date object1 = new Date();
    Output output = new Output(1024, -1);
    kryo.writeObject(output, object1);
    System.out.println("output.position()= "+output.position());

    Input input = new Input(output.getBuffer(), 0, output.position());
    Date object2 = kryo.readObject(input, Date.class);

    assertEquals(object1, object2);

    // compare with SerializableUtils
    byte[] bytes = SerializableUtils.serialize(object1);
    System.out.println("bytes size = "+ bytes.length);
  }

  @Test
  public void testPerson(){
    Kryo kryo = new Kryo();

    System.out.println("reference= "+ kryo.getReferences());
    kryo.register(Date.class);
    kryo.register(Person.class);

    Person object1 = new Person();
    object1.setName("1234567890");

    Output output = new Output(1024, -1);
    kryo.writeObject(output, object1);
    System.out.println("output.position()= "+output.position());              // 18 bytes

    Input input = new Input(output.getBuffer(), 0, output.position());
    Person object2 = kryo.readObject(input, Person.class);

    assertEquals(object1, object2);

    byte[] bytes = SerializableUtils.serialize(object1);
    System.out.println("bytes size = "+ bytes.length);                        // 151 bytes
  }
}
