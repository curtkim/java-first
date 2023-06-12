package org.example;

import org.wololo.flatgeobuf.*;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.Feature;
import org.wololo.flatgeobuf.generated.Geometry;
import org.wololo.flatgeobuf.generated.GeometryType;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ReadAllMainByIterator {
  public static void main(String[] args) throws IOException {
    long startTime = System.currentTimeMillis();

    //File file = new File(System.getProperty("user.home")+ "/Documents/rn_link_l.fgb"); // 6초 소요
    File file = new File("countries.fgb");
    FileChannel ch = new RandomAccessFile(file, "r").getChannel();
    System.out.println("file size: " + ch.size());

    HeaderMeta headerMeta = SearchByRectStream.readMeta(ch);
    printMeta(headerMeta);

    long treeSize = PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("treeSize: " + treeSize);

    int offset = headerMeta.offset + (int)treeSize;
    System.out.println("featureOffset: " +offset);
    System.out.println("featureSize: " + (ch.size()-offset));
    ByteBuffer featureBuffer = ch.map(FileChannel.MapMode.READ_ONLY, offset, ch.size()-offset);
    featureBuffer.order(ByteOrder.LITTLE_ENDIAN);

    Iterator<SimpleFeature> iter = new FlatgeobufIterator(headerMeta, featureBuffer);
    while (iter.hasNext()){
      SimpleFeature sf = iter.next();
      //System.out.println(sf.properties + " " + sf.geometry);
    }
    System.out.println("elapsed time " + (System.currentTimeMillis() - startTime) + "ms");
  }

  public static void fillGeometry(HeaderMeta headerMeta, Feature feature, SimpleFeature sf) {
    Geometry geometry = feature.geometry();
    if (geometry != null) {
      byte geometryType = headerMeta.geometryType;
      if (geometryType == GeometryType.Unknown) {
        geometryType = (byte) geometry.type();
      }
      org.locationtech.jts.geom.Geometry jtsGeometry =
          GeometryConversions.deserialize(geometry, geometryType);
      sf.geometry = jtsGeometry;
    }
  }

  public static void fillAttributes(HeaderMeta headerMeta, Feature feature, SimpleFeature sf) {
    int propertiesLength = feature.propertiesLength();
    if (propertiesLength > 0) {
      ByteBuffer pbb = feature.propertiesAsByteBuffer();
      while (pbb.hasRemaining()) {
        short idx = pbb.getShort();
        ColumnMeta columnMeta = headerMeta.columns.get(idx);
        String name = columnMeta.name;
        byte type = columnMeta.type;
        if (type == ColumnType.Bool) sf.properties.put(name, pbb.get() > 0 ? true : false);
        else if (type == ColumnType.Byte) sf.properties.put(name, pbb.get());
        else if (type == ColumnType.Short) sf.properties.put(name, pbb.getShort());
        else if (type == ColumnType.Int) sf.properties.put(name, pbb.getInt());
        else if (type == ColumnType.Long) sf.properties.put(name, pbb.getLong());
        else if (type == ColumnType.Double) sf.properties.put(name, pbb.getDouble());
        else if (type == ColumnType.DateTime) sf.properties.put(name, readString(pbb, name));
        else if (type == ColumnType.String) sf.properties.put(name, readString(pbb, name));
        else throw new RuntimeException("Unknown type");
      }
    }
  }

  private static String readString(ByteBuffer bb, String name) {
    int length = bb.getInt();
    byte[] stringBytes = new byte[length];
    bb.get(stringBytes, 0, length);
    String value = new String(stringBytes, StandardCharsets.UTF_8);
    return value;
  }

  public static void skipNBytes(InputStream stream, long skip) throws IOException {
    long actual = 0;
    long remaining = skip;
    while (actual < remaining) {
      remaining -= stream.skip(remaining);
    }
  }

  public static void printMeta(HeaderMeta headerMeta){
    for (ColumnMeta column : headerMeta.columns)
      System.out.println(String.format("\t%s type=%s width=%d scale=%d precision=%d", column.name, ColumnType.names[column.type], column.width, column.scale, column.precision));
    System.out.println("headerMeta.offset: " + headerMeta.offset);
    System.out.println("headerMeta.featuresCount: " + headerMeta.featuresCount);
    System.out.println("headerMeta.indexNodeSize: " + headerMeta.indexNodeSize);
    System.out.println(headerMeta.envelope);
  }
}