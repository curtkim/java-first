package org.example;

import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.LittleEndianDataInputStream;
import org.wololo.flatgeobuf.PackedRTree;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.List;

public class SearchByRect {

  public static void main(String[] args) throws IOException {
    Envelope env = new Envelope(12, 13, 56, 57);

    File file = new File("countries.fgb");
    byte[] bytes = Files.readAllBytes(file.toPath());
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.order(ByteOrder.LITTLE_ENDIAN);

    HeaderMeta headerMeta = HeaderMeta.read(bb);

    int treeSize = (int)PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);

    /*
    System.out.println(bb.position());
    System.out.println(headerMeta.offset);
    ByteBuffer treeBuffer = ByteBuffer.wrap(bytes, headerMeta.offset, treeSize);
    */
    /*
    byte[] treeBytes = new byte[treeSize];
    bb.get(treeBytes, 0, treeSize);
    InputStream stream = new ByteArrayInputStream(treeBytes);
    */
    List<PackedRTree.SearchHit> results = PackedRTree.search(bb, headerMeta.offset, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, env);

    int featuresOffset = headerMeta.offset + treeSize;
    for(PackedRTree.SearchHit hit : results) {
      // TODO
    }
  }
}
