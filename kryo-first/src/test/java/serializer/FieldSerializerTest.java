package serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serializer.model.Data1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldSerializerTest {

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
  public void test(){
    FieldSerializer<Data1> fieldSerializer = new FieldSerializer<Data1>(kryo, Data1.class);
    kryo.register(Data1.class, fieldSerializer);

    Data1 data1 = new Data1("kim", 20);
    fieldSerializer.write(kryo, output, data1);
    assertEquals(4, output.position());
    output.close();

    Data1 read = fieldSerializer.read(kryo, input, Data1.class);
    input.close();

    assertEquals(read.name, "kim");

    assertEquals(4, Util.getFileSize("file.dat"));
  }
}
