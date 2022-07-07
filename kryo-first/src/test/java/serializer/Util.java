package serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {

public static long getFileSize(String filename){
  Path path = Paths.get(filename);
  try {
    return Files.size(path);
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}
}
