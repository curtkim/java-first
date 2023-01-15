package com.example;

import com.example.tutorial.Msg;
import com.example.tutorial.SecondMsg;

import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException {

    String filename = "sample.data";

    {
      Msg msg = Msg.newBuilder().setFoo("test").setBlah(SecondMsg.newBuilder().setBlah(1).build()).build();
      System.out.println(msg);

      FileOutputStream output = new FileOutputStream(filename);
      msg.writeTo(output);
      output.close();
    }

    {
      Msg msg = Msg.parseFrom(new FileInputStream(filename));
      System.out.println(msg);
    }
  }
}
