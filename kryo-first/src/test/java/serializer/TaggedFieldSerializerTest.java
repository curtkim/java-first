package serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import org.junit.jupiter.api.Assertions;
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

public class TaggedFieldSerializerTest {

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
    TaggedFieldSerializer<Data1> taggedFieldSerializer = new TaggedFieldSerializer<Data1>(kryo, Data1.class);
    kryo.register(Data1.class, taggedFieldSerializer);

    Data1 data1 = new Data1("kim", 20);
    taggedFieldSerializer.write(kryo, output, data1);
    assertEquals(7, output.position());
    output.close();

    Data1 readed = taggedFieldSerializer.read(kryo, input, Data1.class);
    input.close();

    assertEquals(readed.name, "kim");

    assertEquals(7, Util.getFileSize("file.dat"));
  }

  @Test
  public void test_write_version1_read_version2(){
    kryo.register(SEX.class);

    TaggedFieldSerializer<Data1> taggedFieldSerializer1 = new TaggedFieldSerializer<>(kryo, Data1.class);
    kryo.register(Data1.class, taggedFieldSerializer1);

    TaggedFieldSerializer<Data2> taggedFieldSerializer2 = new TaggedFieldSerializer<>(kryo, Data2.class);
    kryo.register(Data2.class, taggedFieldSerializer2);

    Data1 data1 = new Data1("kim", 20);
    taggedFieldSerializer1.write(kryo, output, data1);
    output.close();

    Data2 readed = taggedFieldSerializer2.read(kryo, input, Data2.class);
    input.close();

    assertEquals(readed.name, "kim");
    assertEquals(readed.age, 20);
    assertEquals(readed.sex, SEX.WOMAN);

    assertEquals(7, Util.getFileSize("file.dat"));
  }

  @Test
  public void test_write_version2_read_version1(){
    kryo.register(SEX.class);

    TaggedFieldSerializer<Data1> taggedFieldSerializer1 = new TaggedFieldSerializer<>(kryo, Data1.class);
    taggedFieldSerializer1.getTaggedFieldSerializerConfig().setChunkedEncoding(true);
    taggedFieldSerializer1.getTaggedFieldSerializerConfig().setReadUnknownTagData(true);
    kryo.register(Data1.class, taggedFieldSerializer1);

    TaggedFieldSerializer<Data2> taggedFieldSerializer2 = new TaggedFieldSerializer<>(kryo, Data2.class);
    taggedFieldSerializer2.getTaggedFieldSerializerConfig().setChunkedEncoding(true);
    taggedFieldSerializer2.getTaggedFieldSerializerConfig().setReadUnknownTagData(true);
    kryo.register(Data2.class, taggedFieldSerializer2);

    Data2 data2 = new Data2("kim", 20, SEX.MAN);
    taggedFieldSerializer2.write(kryo, output, data2);
    output.close();

    Data1 readed = taggedFieldSerializer1.read(kryo, input, Data1.class);
    assertEquals(readed.name, "kim");
    assertEquals(readed.age, 20);

    assertEquals(18, Util.getFileSize("file.dat"));
  }
}
