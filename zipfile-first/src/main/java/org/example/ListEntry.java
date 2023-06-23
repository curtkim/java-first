package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

// https://stackoverflow.com/questions/51920911/how-can-i-unzip-huge-folder-with-multithreading-with-java-preferred-java8
public class ListEntry {
  public static void main(String[] args) throws IOException {
    String home= System.getProperty("user.home");
    String zipFilename = "nz-building-outlines-extract.shp.zip";
    String entryName = "nz-building-outlines.prj";
    Path zipPath = Path.of(home, "Downloads", zipFilename);


    try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {
      zipFile.stream()
          .map(ZipEntry::getName)
          .forEach(System.out::println);

      ZipEntry zipEntry = zipFile.getEntry(entryName);
      InputStream is = zipFile.getInputStream(zipEntry);
      is.transferTo(System.out);
    }

//    try (FileSystem fileSystem = FileSystems.newFileSystem(zipPath, new HashMap<>())) {
//      Path fileToExtract = fileSystem.getPath(fileName);
//      Files.copy(fileToExtract, outputFile);
//    }
  }
}