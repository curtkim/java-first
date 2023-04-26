import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import object.Person;

import java.util.Date;

public class PersonSerializer extends Serializer<Person> {

  public void write(Kryo kryo, Output output, Person object) {
    // age는 write하지 않는다.
    output.writeString(object.getName());
    output.writeLong(object.getBirthDate().getTime());
  }

  @Override
  public Person read(Kryo kryo, Input input, Class<? extends Person> type) {
    String name = input.readString();
    long birthDate = input.readLong();
    return new Person(name, new Date(birthDate));
  }

  //public object.Person copy(Kryo kryo, object.Person original) { }
}
