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

    HeaderMeta headerMeta = HeaderMeta.read(bb);
    {
      System.out.println("file size: " + bytes.length);
      System.out.println("headerMeta.offset: " + headerMeta.offset);
      System.out.println("headerMeta.featuresCount: " + headerMeta.featuresCount);
      for (ColumnMeta column : headerMeta.columns)
        System.out.println(String.format("\t%s type=%s width=%d scale=%d precision=%d", column.name, ColumnType.names[column.type], column.width, column.scale, column.precision));
      System.out.println(headerMeta.envelope);
    }

    int treeSize = (int)PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("treeSize: " + treeSize);

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

    //serach( byteBuffer, start, numItems, nodeSize, env)
    Envelope env = new Envelope(127, 128, 36, 37);
    List<PackedRTree.SearchHit> results = PackedRTree.search(bb, headerMeta.offset, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, env);

    int featuresOffset = headerMeta.offset + treeSize;
    System.out.println("hit");

    Feature feature = new Feature();
    for(PackedRTree.SearchHit hit : results) {
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
