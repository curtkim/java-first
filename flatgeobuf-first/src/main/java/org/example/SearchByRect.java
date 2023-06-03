package org.example;

import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.ColumnMeta;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.LittleEndianDataInputStream;
import org.wololo.flatgeobuf.PackedRTree;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.Feature;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.List;

public class SearchByRect {

  public static void main(String[] args) throws IOException {
    File file = new File("countries.fgb");
    byte[] bytes = Files.readAllBytes(file.toPath());
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    System.out.println("bytes.length: " + bytes.length);

    HeaderMeta headerMeta = HeaderMeta.read(bb);
    ReadAllMain.printMeta(headerMeta);

    int treeSize = (int)PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("treeSize: " + treeSize);
    System.out.println("tree node count: " + (treeSize / (8 * 4 + 8)));


    //serach( byteBuffer, start, numItems, nodeSize, env)
    Envelope koreaEnv = new Envelope(127, 128, 36, 37);
    List<PackedRTree.SearchHit> results = PackedRTree.search(bb, headerMeta.offset, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, koreaEnv);

    int featuresOffset = headerMeta.offset + treeSize;

    Feature feature = new Feature();
    for(PackedRTree.SearchHit hit : results) {
      System.out.println("===================================");
      System.out.println(String.format("offset=%d index=%d", hit.offset, hit.index));

      bb.position(featuresOffset + (int)hit.offset);
      int featureSize = bb.getInt();
      System.out.println("featureSize: " + featureSize);
      Feature.getRootAsFeature(bb, feature);
      //Feature feature = Feature.getRootAsFeature(bb);

      SimpleFeature sf = new SimpleFeature();
      ReadAllMain.fillGeometry(headerMeta, feature, sf);
      ReadAllMain.fillAttributes(headerMeta, feature, sf);
      System.out.println(sf.geometry);
      System.out.println(sf.properties);
    }
  }
}
