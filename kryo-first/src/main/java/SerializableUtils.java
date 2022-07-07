
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SerializableUtils {

  public static byte[] serialize(Serializable obj) {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeObject(obj);
      oos.close();
      os.close();
      return os.toByteArray();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static byte[] serializeWithCompression(Serializable obj) {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      GZIPOutputStream gos = new GZIPOutputStream(os);
      ObjectOutputStream oos = new ObjectOutputStream(gos);
      oos.writeObject(obj);
      oos.close();
      gos.close();
      os.close();
      return os.toByteArray();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Serializable deserialize(byte[] bytes) {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    try {
      ObjectInputStream ois = new ObjectInputStream(bais);
      Serializable result = (Serializable)ois.readObject();
      ois.close();
      bais.close();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Serializable deserializeWithCompress(byte[] bytes) {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    try {
      GZIPInputStream gis = new GZIPInputStream(bais);
      ObjectInputStream ois = new ObjectInputStream(gis);
      Serializable result = (Serializable)ois.readObject();
      ois.close();
      gis.close();
      bais.close();
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isCompressed(byte[] bytes)
  {
    if ((bytes == null) || (bytes.length < 2))
    {
      return false;
    }
    else
    {
      return ((bytes[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
    }
  }

}

