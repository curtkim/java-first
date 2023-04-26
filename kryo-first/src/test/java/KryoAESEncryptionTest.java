import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BlowfishSerializer;
import object.Person;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KryoAESEncryptionTest {

  @Test
  public void test(){
    Kryo kryo = new Kryo();

    kryo.register(Date.class);
    // BlowfishSerializer를 사용하면 결과가 byte가 증가한다.
    kryo.register(Person.class, new BlowfishSerializer(kryo.getDefaultSerializer(Person.class), new byte[]{1,2,3,4,5,6,7,8}));
    //kryo.register(Person.class);

    Person object1 = new Person();
    object1.setName("1234567890");

    Output output = new Output(1024, -1);
    kryo.writeObject(output, object1);
    System.out.println("output.position()= "+output.position());              // 18 bytes

    Input input = new Input(output.getBuffer(), 0, output.position());
    Person object2 = kryo.readObject(input, Person.class);

    assertEquals(object1, object2);
  }
}
