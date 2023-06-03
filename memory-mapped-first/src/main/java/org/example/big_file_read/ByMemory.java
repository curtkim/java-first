package org.example.big_file_read;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ByMemory {
  private static final String FILE_NAME = "links_capital_mesh_conn3.txt";

  public static void main(String[] args) throws IOException {
    long startTime = System.currentTimeMillis();
    RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw");
    FileChannel channel = file.getChannel();

    // Read file into mapped buffer
    ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());

    System.out.println("Reading content and printing ... " + channel.size() + " bytes");
    for (int i = 0; i < channel.size(); i++) {
      //System.out.print((char) mbb.get());
      buffer.get();
      if( i % 10000000 == 0)
        System.out.println(String.format("%d total=%d free=%d", i, Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory()));
    }

    channel.close();
    file.close();
    System.out.println(String.format("Total read and print time: %d ms ", (System.currentTimeMillis() - startTime)));
  }
}
