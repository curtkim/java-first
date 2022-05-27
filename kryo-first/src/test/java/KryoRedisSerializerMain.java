public class KryoRedisSerializerMain {
  public static void main(String[] args){
    KryoRedisSerializer<String> target = new KryoRedisSerializer<>();
    byte[] bytes = target.serialize(
        "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789" +
            "0123456789"
        );
    System.out.println(bytes.length);
  }
}
