package serializer.model;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class Data1 {

  @Tag(1)
  public String name;
  @Tag(2)
  public int age;


  public Data1(){}

  public Data1(String name, int age) {
    this.name = name;
    this.age = age;
  }
}
