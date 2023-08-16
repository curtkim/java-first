package org.example;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Map;

import static org.carlspring.cloud.storage.s3fs.S3Factory.*;

public class Main {
  public static void main(String[] args) throws IOException {
    Map<String, ?> env = Map.of(
        ACCESS_KEY, "myS3",
        SECRET_KEY, "myS3Pass",
        PROTOCOL, "https");

    FileSystem fs = FileSystems.newFileSystem(URI.create("s3://localhost.localstack.cloud:4566/"),
        env,
        Thread.currentThread().getContextClassLoader());

    //System.out.println(fs.isOpen());
    Path path = fs.getPath("/temp");
    Path localFile = Paths.get("build.gradle");
    Path s3File = fs.getPath("/temp/build.gradle");

    //mkdir(path);
    Files.copy(localFile, s3File, StandardCopyOption.REPLACE_EXISTING);

    Files.lines(s3File)
        .forEach(System.out::println);
    System.out.println(s3File.toAbsolutePath());
  }

  static void mkdir(Path path) {
    try {
      Files.createDirectories(path);
      System.out.println("Directory is created!");
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }
  }
}