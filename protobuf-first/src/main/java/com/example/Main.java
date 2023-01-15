package com.example;

import com.example.tutorial.Msg;
import com.example.tutorial.MsgList;
import com.example.tutorial.SecondMsg;

import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException {

    String filename = "sample.data";

    {
      MsgList list = MsgList.newBuilder()
          .addMsg(Msg.newBuilder().setFoo("test").setBlah(SecondMsg.newBuilder().setBlah(1).build()).build())
          .addMsg(Msg.newBuilder().setFoo("test2").setBlah(SecondMsg.newBuilder().setBlah(2).build()).build())
          .build();

      System.out.println(list);

      FileOutputStream output = new FileOutputStream(filename);
      list.writeTo(output);
      output.close();
    }

    {
      MsgList msgList = MsgList.parseFrom(new FileInputStream(filename));
      System.out.println(msgList);
    }
  }
}
