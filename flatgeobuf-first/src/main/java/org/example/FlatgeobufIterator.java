package org.example;

import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.generated.Feature;

import java.nio.ByteBuffer;
import java.util.Iterator;

public class FlatgeobufIterator implements Iterator<SimpleFeature> {

  private HeaderMeta headerMeta;
  private ByteBuffer byteBuffer;

  public FlatgeobufIterator(HeaderMeta headerMeta, ByteBuffer byteBuffer) {
    this.headerMeta = headerMeta;
    this.byteBuffer = byteBuffer;
  }

  @Override
  public boolean hasNext() {
    return byteBuffer.position() < byteBuffer.capacity();
  }

  @Override
  public SimpleFeature next() {
    int featureSize = byteBuffer.getInt();
    Feature feature = Feature.getRootAsFeature(byteBuffer);
    SimpleFeature sf = new SimpleFeature();
    ReadAllMain.fillGeometry(headerMeta, feature, sf);
    ReadAllMain.fillAttributes(headerMeta, feature, sf);
    byteBuffer.position( byteBuffer.position()+featureSize);
    return sf;
  }
}
