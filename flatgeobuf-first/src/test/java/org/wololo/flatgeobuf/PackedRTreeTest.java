package org.wololo.flatgeobuf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.NodeItem;
import org.wololo.flatgeobuf.PackedRTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PackedRTreeTest {

  List<PackedRTree.FeatureItem> nodeItemList = new LinkedList<>();
  List<PackedRTree.FeatureItem> sortNodeItemList = new LinkedList<>();
  NodeItem extend = new NodeItem(0);

  @BeforeEach
  public void setUp() throws Exception {
    PackedRTree.FeatureItem featureItem = new PackedRTree.FeatureItem();
    featureItem.nodeItem = new NodeItem(2.1, 2.1, 8.5, 5.5, 1000);
    PackedRTree.FeatureItem featureItem2 = new PackedRTree.FeatureItem();
    nodeItemList.add(featureItem);
    featureItem2.nodeItem = new NodeItem(10, 2.1, 12, 5.5, 500);
    nodeItemList.add(featureItem2);
    PackedRTree.FeatureItem featureItem3 = new PackedRTree.FeatureItem();
    featureItem3.nodeItem = new NodeItem(10, 3, 12, 6, 200);
    nodeItemList.add(featureItem3);
    sortNodeItemList.add(featureItem);
    sortNodeItemList.add(featureItem3);
    sortNodeItemList.add(featureItem2);
    nodeItemList.forEach(x -> extend.expand(x.nodeItem));
  }

  @Test
  public void testHibert(){
    long v1 = PackedRTree.hibert(nodeItemList.get(0).nodeItem, PackedRTree.HILBERT_MAX, extend.minX, extend.minY, extend.width(), extend.height());
    long v2 = PackedRTree.hibert(nodeItemList.get(1).nodeItem, PackedRTree.HILBERT_MAX, extend.minX, extend.minY, extend.width(), extend.height());
    long v3 = PackedRTree.hibert(nodeItemList.get(2).nodeItem, PackedRTree.HILBERT_MAX, extend.minX, extend.minY, extend.width(), extend.height());

    System.out.println(v1);
    System.out.println(v2);
    System.out.println(v3);
  }

  @Test
  public void testHilbertSort() {
    assertEquals(sortNodeItemList, PackedRTree.hilbertSort(nodeItemList, extend));
    assertEquals(sortNodeItemList, PackedRTree.hilbertSort(nodeItemList, extend));
  }

  @Test
  public void testCalcExtent() {
    assertEquals(extend, PackedRTree.calcExtent(nodeItemList));
    assertEquals(extend, PackedRTree.calcExtent(sortNodeItemList));
  }

  @Test
  public void testGenerateLevelBounds() {
    List<PackedRTree.Pair<Integer, Integer>> list = new LinkedList<>();
    list.add(new PackedRTree.Pair<>(3, 23));
    list.add(new PackedRTree.Pair<>(1, 3));
    list.add(new PackedRTree.Pair<>(0, 1));
    assertEquals(list, PackedRTree.generateLevelBounds(20, 16));

    list = new LinkedList<>();
    list.add(new PackedRTree.Pair<>(1, 17));
    list.add(new PackedRTree.Pair<>(0, 1));
    assertEquals(list, PackedRTree.generateLevelBounds(16, 16));

    list = new LinkedList<>();
    list.add(new PackedRTree.Pair<>(1, 4));
    list.add(new PackedRTree.Pair<>(0, 1));
    assertEquals(list, PackedRTree.generateLevelBounds(3, 16));
  }

  @Test
  public void testWrite() throws IOException {
    File tmpFile = new File("tmp20221102.fgb");
    tmpFile.deleteOnExit();
    tmpFile.createNewFile();
    try (FileOutputStream outputStream = new FileOutputStream(tmpFile);) {
      PackedRTree packedRTree = new PackedRTree(sortNodeItemList, (short) 16);
      packedRTree.write(outputStream);
    }
    try (FileInputStream fileInputStream = new FileInputStream(tmpFile)) {
      PackedRTree.SearchResult searchResult = PackedRTree.search(fileInputStream, 0, 3, 16, new Envelope(10, 12, 2.1, 2.999));
      assertEquals(1, searchResult.hits.size());
      assertEquals(sortNodeItemList.get(2).nodeItem.offset, searchResult.hits.get(0).offset);
    }
    tmpFile.deleteOnExit();
  }
}
