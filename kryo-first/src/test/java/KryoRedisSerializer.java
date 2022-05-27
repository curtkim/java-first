import com.esotericsoftware.kryo.Kryo ;
import com.esotericsoftware.kryo.io.Input ;
import com.esotericsoftware.kryo.io.Output ;
import org.springframework.data.redis.serializer.RedisSerializer ;
import org.springframework.data.redis.serializer.SerializationException ;

import java.util.Arrays ;


public class KryoRedisSerializer<T> implements RedisSerializer<T> {
  private Kryo kryo = new Kryo() ;

  @Override
  public byte[] serialize(T t) throws SerializationException {

    System.out.println ( "[serialize]" + t) ;

    byte[] buffer = new byte[ 32] ;
    Output output = new Output(buffer, 2048*10) ;
    kryo.writeClassAndObject (output, t) ;
    return output.toBytes () ;
  }

  @Override
  public T deserialize(byte[] bytes) throws SerializationException {

    System.out.println ( "[deserialize]" + Arrays.toString (bytes)) ;

    Input input = new Input(bytes) ;
    @SuppressWarnings( "unchecked" )
    T t = (T) kryo.readClassAndObject (input) ;
    return t ;
  }

}