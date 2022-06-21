package example;


import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.StandardCopyOption;

public class WriteInputStreamToFile {

  @Test
  public void whenConvertingAnInProgressInputStreamToFile_thenCorrect2() throws IOException {

    InputStream initialStream = new FileInputStream(new File("build.gradle"));
    File targetFile = new File("build.tmp");

    java.nio.file.Files.copy(
        initialStream,
        targetFile.toPath(),
        StandardCopyOption.REPLACE_EXISTING);

    IOUtils.closeQuietly(initialStream);
  }
}
