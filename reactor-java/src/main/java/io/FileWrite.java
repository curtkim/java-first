package io;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileWrite {
  public static void main(String[] args) {
    DefaultDataBufferFactory bufferFactory = new DefaultDataBufferFactory();
    CharSequenceEncoder encoder = CharSequenceEncoder.textPlainOnly();
    Path path = Paths.get("temp.txt");

    Flux<DataBuffer> dataBufferFlux = Flux.just("Hello", "World")
        .map(line -> line + "\n")
        .map(line ->
            encoder.encodeValue(line, bufferFactory, ResolvableType.NONE, null, null)
        );

    DataBufferUtils.write(
            dataBufferFlux,
            path,
            StandardOpenOption.CREATE
        )
        .block();

    // READ
    DataBufferUtils.read(path, bufferFactory, 1024)
        .map((DataBuffer buffer) -> buffer.toString(0, buffer.writePosition(), Charset.defaultCharset()))
        .doOnNext(System.err::println)
        .blockLast();
  }
}
