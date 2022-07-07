package serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serializer.model.Data1;
import serializer.model.Data2;
import serializer.model.SEX;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompatibleFieldSerializerTest {

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
  public void test_data1(){
    CompatibleFieldSerializer<Data1> compatibleFieldSerializer = new CompatibleFieldSerializer<>(kryo, Data1.class);
    kryo.register(Data1.class, compatibleFieldSerializer);

    Data1 data1 = new Data1("kim", 20);
    compatibleFieldSerializer.write(kryo, output, data1);
    assertEquals(14, output.position());
    output.close();

    Data1 readed = (Data1)compatibleFieldSerializer.read(kryo, input, Data1.class);
    input.close();

    assertEquals(readed.name, "kim");
    assertEquals(14, Util.getFileSize("file.dat"));
  }

  @Test
  public void test_write_version1_read_version2(){
    kryo.register(SEX.class);

    CompatibleFieldSerializer<Data1> compatibleFieldSerializer1 = new CompatibleFieldSerializer<>(kryo, Data1.class);
    kryo.register(Data1.class, compatibleFieldSerializer1);

    CompatibleFieldSerializer<Data2> compatibleFieldSerializer2 = new CompatibleFieldSerializer<>(kryo, Data2.class);
    kryo.register(Data2.class, compatibleFieldSerializer2);

    Data1 data1 = new Data1("kim", 20);
    compatibleFieldSerializer1.write(kryo, output, data1);
    output.close();

    Data2 readed = compatibleFieldSerializer2.read(kryo, input, Data2.class);
    input.close();

    assertEquals(readed.name, "kim");
    assertEquals(readed.age, 20);
    assertEquals(readed.sex, SEX.WOMAN);

    assertEquals(14, Util.getFileSize("file.dat"));
  }

  @Test
  public void test_write_version2_read_version1(){
    kryo.register(SEX.class);

    CompatibleFieldSerializer<Data1> compatibleFieldSerializer1 = new CompatibleFieldSerializer<>(kryo, Data1.class);
    kryo.register(Data1.class, compatibleFieldSerializer1);

    CompatibleFieldSerializer<Data2> compatibleFieldSerializer2 = new CompatibleFieldSerializer<>(kryo, Data2.class);
    kryo.register(Data2.class, compatibleFieldSerializer2);

    Data2 data2 = new Data2("kim", 20, SEX.MAN);
    compatibleFieldSerializer2.write(kryo, output, data2);
    output.close();

    Data1 read = compatibleFieldSerializer1.read(kryo, input, Data1.class);
    input.close();

    assertEquals(read.name, "kim");
    assertEquals(read.age, 20);

    assertEquals(19, Util.getFileSize("file.dat"));
  }
}
