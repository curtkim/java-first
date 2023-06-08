package org.example;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {
  public static void main(String[] args) throws IOException {
    Apfloat x = new Apfloat(2.12345, 4);
    System.out.println(x);
    System.out.println(ApfloatMath.sqrt(x));
    System.out.println("size: " +x.size());
    System.out.println("scale: " + x.scale());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream o = new ObjectOutputStream(baos);

    try {
      o.writeObject(x);
      o.close();
      byte[] bytes = baos.toByteArray();
      System.out.println("bytes: " + bytes.length);   // 763
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}