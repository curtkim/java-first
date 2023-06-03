package org.example.big_file_read;

import java.io.*;

public class Normla {

  private static final String FILE_NAME = "links_capital_mesh_conn3.txt";

  public static void main(String[] args) throws IOException {
    long startTime = System.currentTimeMillis();

    InputStream is = new BufferedInputStream(new FileInputStream(FILE_NAME));
    int b = is.read();
    while (b != -1) {
      //System.out.printf("%c", b);
      b = is.read();
    }
    is.close();

    System.out.println(String.format("Total read and print time: %d ms ", (System.currentTimeMillis() - startTime)));
  }
}
