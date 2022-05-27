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

public class kryoTest {
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
  public void givenObject_whenSerializing_thenReadCorrectly() {
    Object someObject = "Some string";

    kryo.writeClassAndObject(output, someObject);
    output.close();

    Object theObject = kryo.readClassAndObject(input);
    input.close();

    assertEquals(theObject, "Some string");
  }

  @Test
  public void givenObjects_whenSerializing_thenReadCorrectly() {
    String someString = "Multiple Objects";
    Date someDate = new Date(915170400000L);

    kryo.writeObject(output, someString);
    kryo.writeObject(output, someDate);
    output.close();

    String readString = kryo.readObject(input, String.class);
    Date readDate = kryo.readObject(input, Date.class);
    input.close();

    assertEquals(readString, "Multiple Objects");
    assertEquals(readDate.getTime(), 915170400000L);
  }
}
