package example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;

public class LocationIndexedLineTest {
  public static void main(String[] args){
    GeometryFactory fac = new GeometryFactory();
    LineString line = fac.createLineString(new Coordinate[]{
      new Coordinate(0, 0),
        new Coordinate(1, 0),
        new Coordinate(1, 1)
    });
    LocationIndexedLine indexedLine = new LocationIndexedLine(line);

    LinearLocation loc = indexedLine.indexOf(new Coordinate(0.5, 0));
    System.out.println(String.format(
        "componentIndex=%d, segmentIndex=%d, segmentFraction=%f",
        loc.getComponentIndex(), loc.getSegmentIndex(), loc.getSegmentFraction()
    ));
  }
}
