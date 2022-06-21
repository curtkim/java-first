// https://www.vinsguru.com/reactor-flux-file-reading/
package example;

import reactor.core.publisher.Flux;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FileRead {
  // private methods to handle checked exceptions

  static void close(Closeable closeable) {
    try {
      closeable.close();
      System.out.println("Closed the resource");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  static void write(BufferedWriter bw, String string) {
    try {
      bw.write(string);
      bw.newLine();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void main(String[] args) throws IOException {
    // input file
    Path ipPath = Paths.get("build.gradle");

    Flux<String> stringFlux = Flux.using(
        () -> Files.lines(ipPath),
        Flux::fromStream,
        (stream) -> {
          System.out.println("resource cleanup");
          stream.close();
        }
    );

    // output file
    Path opPath = Paths.get("/tmp/large-output-file.txt");
    BufferedWriter bw = Files.newBufferedWriter(opPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

    stringFlux
        .subscribe(s -> {
              System.out.println("\t-- " + s);
              write(bw, s);
            },
            (e) -> {
              close(bw);
            },  // close file if error / oncomplete
            () -> {
              System.out.println("complete");
              close(bw);
            }
        );
  }
}
