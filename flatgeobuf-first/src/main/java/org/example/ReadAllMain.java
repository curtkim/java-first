package org.example;

import org.wololo.flatgeobuf.*;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.Feature;
import org.wololo.flatgeobuf.generated.Geometry;
import org.wololo.flatgeobuf.generated.GeometryType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class ReadAllMain {
  public static void main(String[] args) throws IOException {

    File file = new File("countries.fgb");
    InputStream is = new FileInputStream(file);

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

    for (int i = 0; i < headerMeta.featuresCount; i++) {
      int featureSize;
      featureSize = data.readInt();
      byte[] bytes = new byte[featureSize];
      data.readFully(bytes);
      ByteBuffer bb = ByteBuffer.wrap(bytes);
      Feature feature = Feature.getRootAsFeature(bb);

      SimpleFeature sf = new SimpleFeature();

      Geometry geometry = feature.geometry();
      byte geometryType = headerMeta.geometryType;
      if (geometry != null) {
        if (geometryType == GeometryType.Unknown) {
          geometryType = (byte) geometry.type();
        }
        org.locationtech.jts.geom.Geometry jtsGeometry =
            GeometryConversions.deserialize(geometry, geometryType);
        sf.geometry = jtsGeometry;
      }

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
      System.out.println(sf.properties);
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