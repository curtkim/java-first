package com.example.rediskryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class KryoRedisSerializer<T> implements RedisSerializer<T> {
  private Kryo kryo;

  public KryoRedisSerializer(Kryo kryo) {
    this.kryo = kryo;
  }

  @Override
  public byte[] serialize(T t) throws SerializationException {
    Output output = new Output(32, 1024*10);
    kryo.writeClassAndObject(output, t);
    return output.toBytes();
  }

  @Override
  public T deserialize(byte[] bytes) throws SerializationException {
    if( bytes == null)
      return null;

    Input input = new Input(bytes);
    //@SuppressWarnings("unchecked")
    T t = (T)kryo.readClassAndObject(input);
    return t;
  }
}
