package org.wololo.flatgeobuf;

import org.example.ReadAllMain;
import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class PackedRTreeMain {

  public static void main(String[] args) throws IOException {
    File file = new File("countries.fgb");
    byte[] bytes = Files.readAllBytes(file.toPath());
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    System.out.println("bytes.length: " + bytes.length);

    HeaderMeta headerMeta = HeaderMeta.read(bb);
    System.out.println("featureCount: " + headerMeta.featuresCount);
    System.out.println("indexNodeSize: " + headerMeta.indexNodeSize);
    System.out.println("offset: " + headerMeta.offset);


    int treeSize = (int) PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("treeSize: " + treeSize + " bytes");
    System.out.println("tree node count: " + (treeSize / (8 * 4 + 8)));

    Envelope koreaEnv = new Envelope(127, 128, 36, 37);

    ByteBuffer bb2 = ByteBuffer.allocate(bytes.length - headerMeta.offset);
    bb2.put(bytes, headerMeta.offset, bytes.length - headerMeta.offset);
    bb2.order(ByteOrder.LITTLE_ENDIAN);
    List<PackedRTree.SearchHit> results = search(bb2, 0, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, koreaEnv);
  }


  static ArrayList<PackedRTree.SearchHit> search(ByteBuffer bb, int start, int numItems, int nodeSize, Envelope rect) {
    final int NODE_ITEM_LEN = 8 * 4 + 8;

    ArrayList<PackedRTree.SearchHit> searchHits = new ArrayList<PackedRTree.SearchHit>();
    double minX = rect.getMinX();
    double minY = rect.getMinY();
    double maxX = rect.getMaxX();
    double maxY = rect.getMaxY();
    ArrayList<Integer> levelEnds = PackedRTree.generateLevelEnds(numItems, nodeSize);
    System.out.println("levelEnds: " +levelEnds);
    int numNodes = levelEnds.get(0);
    Stack<StackItem> stack = new Stack<>();
    stack.add(new StackItem(0, levelEnds.size() - 1));
    while (stack.size() != 0) {
      StackItem stackItem = stack.pop();
      int nodeIndex = (int) stackItem.nodeIndex;
      int level = stackItem.level;

      boolean isLeafNode = nodeIndex >= numNodes - numItems;

      // find the end index of the node
      int levelEnd = levelEnds.get(level);
      int end = Math.min(nodeIndex + nodeSize, levelEnd);
      int nodeStart = start + (nodeIndex * NODE_ITEM_LEN);
      // int length = end - nodeIndex;
      // search through child nodes
      for (int pos = nodeIndex; pos < end; pos++) {
        int offset = nodeStart + ((pos - nodeIndex) * NODE_ITEM_LEN);
        System.out.println(String.format("level=%d, pos=%d, nodeIndex=%d, end=%d, isLeafNode=%b", level, pos, nodeIndex, end, isLeafNode));
        double nodeMinX = bb.getDouble(offset + 0);
        double nodeMinY = bb.getDouble(offset + 8);
        double nodeMaxX = bb.getDouble(offset + 16);
        double nodeMaxY = bb.getDouble(offset + 24);
        //System.out.println(String.format("minx=%f minY=%f maxX=%f maxY=%f", nodeMinX, nodeMinY, nodeMaxX, nodeMaxY));
        if (maxX < nodeMinX)
          continue;
        if (maxY < nodeMinY)
          continue;
        if (minX > nodeMaxX)
          continue;
        if (minY > nodeMaxY)
          continue;
        System.out.println("pass");
        long indexOffset = bb.getLong(offset + 32);
        if (isLeafNode)
          searchHits.add(new PackedRTree.SearchHit(indexOffset, pos - 1));
        else
          stack.add(new StackItem(indexOffset, level - 1));
      }
    }
    return searchHits;
  }
}
class StackItem {
  public StackItem(long nodeIndex, int level) {
    this.nodeIndex = nodeIndex;
    this.level = level;
  }

  long nodeIndex;
  int level;
}
