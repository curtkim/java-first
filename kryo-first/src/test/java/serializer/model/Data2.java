package serializer.model;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class Data2 {

  @Tag(1)
  public String name;
  @Tag(2)
  public int age;

  @Tag(3)
  public SEX sex = SEX.WOMAN;


  public Data2(){}

  public Data2(String name, int age, SEX sex) {
    this.name = name;
    this.age = age;
    this.sex = sex;
  }
}
