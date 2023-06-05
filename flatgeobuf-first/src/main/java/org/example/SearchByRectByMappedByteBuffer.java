package org.example;

import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;
import org.wololo.flatgeobuf.generated.Feature;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class SearchByRectByMappedByteBuffer {

  public static void main(String[] args) throws IOException {
    String filename = System.getProperty("user.home") + "/Documents/rn_link_l.fgb";
    List<Envelope> envList = Arrays.asList(
        new Envelope(209700, 210200, 433200, 433600),
        new Envelope(209700, 210200, 433200, 433600)
    );

    //Envelope env = new Envelope(127, 128, 36, 37);

    RandomAccessFile file = new RandomAccessFile(filename, "r");
    FileChannel channel = file.getChannel();

    //File file = new File("countries.fgb");
    //byte[] bytes = Files.readAllBytes(file.toPath());
    //ByteBuffer bb = ByteBuffer.wrap(bytes);
    ByteBuffer bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
    bb.order(ByteOrder.LITTLE_ENDIAN);
    System.out.println("channel.size: " + channel.size());

    HeaderMeta headerMeta = HeaderMeta.read(bb);
    ReadAllMain.printMeta(headerMeta);

    int treeSize = (int) PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("treeSize: " + treeSize);
    System.out.println("tree node count: " + (treeSize / (8 * 4 + 8)));


    int featuresOffset = headerMeta.offset + treeSize;

    for (Envelope env : envList) {
      //serach( byteBuffer, start, numItems, nodeSize, env)
      List<PackedRTree.SearchHit> results = PackedRTree.search(bb, headerMeta.offset, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, env);
      Feature feature = new Feature();
      for (PackedRTree.SearchHit hit : results) {
        System.out.println("===================================");
        System.out.println(String.format("offset=%d index=%d", hit.offset, hit.index));

        // position을 이동한다.
        bb.position(featuresOffset + (int) hit.offset);
        int featureSize = bb.getInt();
        //System.out.println("featureSize: " + featureSize);
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
}
