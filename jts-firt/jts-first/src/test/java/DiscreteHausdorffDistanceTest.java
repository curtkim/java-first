import org.junit.jupiter.api.Test;
import org.locationtech.jts.algorithm.distance.DiscreteHausdorffDistance;
import org.locationtech.jts.algorithm.match.HausdorffSimilarityMeasure;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class DiscreteHausdorffDistanceTest {

  @Test
  public void distance(){
    GeometryFactory fac = new GeometryFactory();
    LineString line1 = fac.createLineString(new Coordinate[]{
        new Coordinate(0, 0),
        new Coordinate(1, 0)
    });
    LineString line2 = fac.createLineString(new Coordinate[]{
        new Coordinate(0, 1),
        new Coordinate(1, 1)
    });
    System.out.println(DiscreteHausdorffDistance.distance(line1, line2));

    HausdorffSimilarityMeasure measure = new HausdorffSimilarityMeasure();
    System.out.println(measure.measure(line1, line2));
  }
}
