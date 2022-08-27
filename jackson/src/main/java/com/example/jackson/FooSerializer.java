package com.example.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class FooSerializer extends StdSerializer<Foo> {

  public FooSerializer(){
    this(null);
  }

  public FooSerializer(Class<Foo> t) {
    super(t);
  }

  @Override
  public void serialize(Foo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("name", value.name);
    gen.writeNumberField("identity", value.id);
    gen.writeEndObject();
  }
}
