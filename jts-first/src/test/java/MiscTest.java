import org.junit.jupiter.api.Test;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Coordinate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiscTest {

  final double DELTA = 0.00001;

  @Test
  public void testAngle(){
    assertEquals(
        45.0,
        Angle.angle(new Coordinate(1,1) ) * 180 / Math.PI,
        DELTA
    );

  }

}
