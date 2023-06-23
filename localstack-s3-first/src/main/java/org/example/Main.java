package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Main {
  public static void main(String[] args) throws IOException {
    System.setProperty("aws.accessKeyId", "myname");
    System.setProperty("aws.secretAccessKey", "myS3Pass");
    //System.setProperty("") // entrypoint 설정할 수 있으면 좋은데 ㅠㅠ

    FileSystem fs = FileSystems.newFileSystem(URI.create("s3:///"),
        new HashMap<>(),
        Thread.currentThread().getContextClassLoader());
    System.out.println(fs.isOpen());
    System.out.println(fs.getRootDirectories());
    Path path = fs.getPath("/", "shared", "build.gradle.kts");
    System.out.println(path);
    System.out.println(Files.exists(path));

    /*
    Path path = Path.of("s3://shared/build.gradle.kts");
    InputStream is = Files.newInputStream(path);
    is.transferTo(System.out);
     */
  }
}