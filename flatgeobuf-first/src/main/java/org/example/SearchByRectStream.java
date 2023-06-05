package org.example;

import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;
import org.wololo.flatgeobuf.generated.Feature;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.List;

public class SearchByRectStream {

  static HeaderMeta readMeta(FileChannel ch) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(8+4);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    ch.read(buf);
    buf.position(8);
    int len = buf.getInt();

    ByteBuffer bb = ByteBuffer.allocateDirect(8 + 4 + len);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    bb.mark();
    ch.position(0);
    ch.read(bb);
    bb.reset();
    return HeaderMeta.read(bb);
  }

  // ByteBuffer를 3번 만든다.
  // HeaderMeta
  // Tree
  // Feature
  public static void main(String[] args) throws IOException {
    //File file = new File(System.getProperty("user.home")+ "/Documents/rn_link_l.fgb");
    File file = new File("countries.fgb");
    FileChannel ch = new RandomAccessFile(file, "r").getChannel();

    HeaderMeta headerMeta = readMeta(ch);
    System.out.println("total file size=" + Files.size(file.toPath()));
    ReadAllMain.printMeta(headerMeta);

    int treeSize = (int) PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("treeSize: " + treeSize);
    System.out.println("tree node count: " + (treeSize / (8 * 4 + 8)));

    ByteBuffer treeBB = ByteBuffer.allocateDirect(treeSize);
    treeBB.order(ByteOrder.LITTLE_ENDIAN);
    ch.read(treeBB);

    //serach( byteBuffer, start, numItems, nodeSize, env)
    Envelope koreaEnv = new Envelope(127, 128, 36, 37);
    // NOTE start를 0으로 준다.
    List<PackedRTree.SearchHit> results = PackedRTree.search(treeBB, 0, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, koreaEnv);
    System.out.println("results.size()=" + results.size());

    int featuresOffset = headerMeta.offset + treeSize;

    Feature feature = new Feature();
    for(PackedRTree.SearchHit hit : results) {
      System.out.println("===================================");
      System.out.println(String.format("offset=%d index=%d", hit.offset, hit.index));

      // position을 이동한다.
      ch.position(featuresOffset + (int)hit.offset);
      int featureSize = readInt(ch);
      System.out.println("featureSize: " + featureSize);
      ByteBuffer featureBB = ByteBuffer.allocate(featureSize);
      featureBB.order(ByteOrder.LITTLE_ENDIAN);
      featureBB.mark();
      ch.read(featureBB);
      featureBB.reset();

      Feature.getRootAsFeature(featureBB, feature);

      SimpleFeature sf = new SimpleFeature();
      ReadAllMain.fillGeometry(headerMeta, feature, sf);
      ReadAllMain.fillAttributes(headerMeta, feature, sf);
      //System.out.println("intersects " +koreaEnv.intersects(sf.geometry.getEnvelopeInternal()));
      System.out.println(sf.geometry);
      System.out.println(sf.properties);
    }

    ch.close();
  }

  static int readInt(ReadableByteChannel ch){
    ByteBuffer buf = ByteBuffer.allocate(4);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    try {
      ch.read(buf);
    } catch (IOException e) {
      e.printStackTrace();
    }
    buf.position(0);
    return buf.getInt();
  }
}
