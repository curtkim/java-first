package org.example.sux;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

  public static List<Long> makeRandomList(int size) {
    return makeRandomList(size, Long.MAX_VALUE);
  }

  public static List<Long> makeRandomList(int size, long maxValue) {
    Set<Long> set = new HashSet<>();
    for (int i = 0; i < size; i++) {
      long v = (long) (Math.random() * maxValue);
      set.add(v);
    }
    return new ArrayList<>(set);
  }

  public static List<Long> makeRandomSortedList(int size, long maxValue){
    List<Long> list = makeRandomList(size, maxValue);
    list.sort(Long::compareTo);
    return list;
  }

  public static List<Long> makeRandomSortedList(int size){
    return makeRandomSortedList(size, Long.MAX_VALUE);
  }


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
