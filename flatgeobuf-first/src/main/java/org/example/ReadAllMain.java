package org.example;

import org.wololo.flatgeobuf.*;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.Feature;
import org.wololo.flatgeobuf.generated.Geometry;
import org.wololo.flatgeobuf.generated.GeometryType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class ReadAllMain {
  public static void main(String[] args) throws IOException {

    //File file = new File("countries.fgb");
    //File file = new File(System.getProperty("user.home")+ "/Documents/rn_link_l.fgb");
    //InputStream is = new FileInputStream(file);
    InputStream is = new GZIPInputStream(new FileInputStream(System.getProperty("user.home") + "/Documents/rn_link_l.fgb.gz"));

    HeaderMeta headerMeta = HeaderMeta.read(is);
    {
      System.out.println("headerMeta.offset: " + headerMeta.offset);
      System.out.println("headerMeta.featuresCount: " + headerMeta.featuresCount);
      for (ColumnMeta column : headerMeta.columns)
        System.out.println(String.format("\t%s %s %d %d %d", column.name, column.type, column.width, column.scale, column.precision));
      System.out.println(headerMeta.envelope);
    }

    long treeSize = PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize);
    System.out.println("size: " + treeSize);

    LittleEndianDataInputStream data = new LittleEndianDataInputStream(is);
    skipNBytes(data, treeSize);

    List<SimpleFeature> results = new ArrayList<>();;
    for (int i = 0; i < headerMeta.featuresCount; i++) {
      int featureSize = data.readInt();
      byte[] bytes = new byte[featureSize];
      data.readFully(bytes);
      ByteBuffer bb = ByteBuffer.wrap(bytes);
      Feature feature = Feature.getRootAsFeature(bb);

      SimpleFeature sf = new SimpleFeature();
      fillGeometry(headerMeta, feature, sf);
      fillAttributes(headerMeta, feature, sf);
      //results.add(sf);
      //System.out.println(sf.properties);
    }
    System.out.println("results.size(): " + results.size());
  }

  private static void fillGeometry(HeaderMeta headerMeta, Feature feature, SimpleFeature sf) {
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

  private static void fillAttributes(HeaderMeta headerMeta, Feature feature, SimpleFeature sf) {
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
        else if (type == ColumnType.DateTime) readString(pbb, name);//sf.properties.put(name, readString(pbb, name));
        else if (type == ColumnType.String) readString(pbb, name);//sf.properties.put(name, readString(pbb, name));
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
}