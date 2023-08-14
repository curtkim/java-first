package example;

import java.io.*;

public class Utils {
  public static void writeFile(String file, Serializable obj) throws IOException {
    ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file));
    o.writeObject(obj);
    o.close();
  }

  public static Serializable readFile(String file) throws IOException, ClassNotFoundException {
    ObjectInputStream o = new ObjectInputStream(new FileInputStream(file));
    Serializable obj = (Serializable) o.readObject();
    o.close();
    return obj;
  }
}
