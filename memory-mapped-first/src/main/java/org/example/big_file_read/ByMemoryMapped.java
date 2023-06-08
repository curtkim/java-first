package org.example.big_file_read;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ByMemoryMapped {
  private static final String FILE_NAME = "links_mesh_conn3.txt";

  public static void main(String[] args) throws IOException {
    long startTime = System.currentTimeMillis();
    RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r");

    FileChannel channel = file.getChannel();
    System.out.println("Reading content and printing ... " + channel.size() + " bytes");

    // Read file into mapped buffer
    MappedByteBuffer mbb =
        channel.map(FileChannel.MapMode.READ_ONLY,
            0,          // position
            channel.size());
    System.out.println("capacity " + mbb.capacity());

    byte[] bytes = new byte[2];
    while(mbb.hasRemaining()) {
      mbb.get(bytes, 0, Math.min(mbb.capacity() - mbb.position(), bytes.length));
    }
    System.out.println(String.format("total=%d free=%d", Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory()));

    channel.close();
    file.close();
    System.out.println("Total read and print time: " + (System.currentTimeMillis() - startTime));
  }
}
