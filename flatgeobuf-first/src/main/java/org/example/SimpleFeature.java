package org.example;

import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.Map;

public class SimpleFeature {
  public Geometry geometry;
  public Map<String, Object> properties = new HashMap<>();

  @Override
  public String toString() {
    return "SimpleFeature{" +
        "properties=" + properties +
        ", geometry=" + geometry +
        '}';
  }
}
