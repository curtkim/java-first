import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Time;
import javax.measure.quantity.Speed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.SECOND;

public class MyTest {

  @Test
  public void testSpeed() {
    Quantity<Length> distance = Quantities.getQuantity(2, METRE);
    Quantity<Time> duration = Quantities.getQuantity(1, SECOND);

    Unit<Speed> unit = Units.METRE_PER_SECOND;
    Unit<Speed> speedUnit = METRE.divide(SECOND).asType(Speed.class);
    assertEquals("m/s", speedUnit.toString());

    Quantity<Speed> q = distance.divide(duration).asType(Speed.class);
    assertEquals(speedUnit, q.getUnit());
    System.out.println(speedUnit.getDimension());
    System.out.println(speedUnit.getBaseUnits());
    //assertEquals("m/s", speedUnit.getName());

    assertEquals("m", METRE.getSymbol());
    assertEquals("s", SECOND.getSymbol());

  }

  @Test
  public void test() {
    Quantity<Speed> C = Quantities.getQuantity(1079252849, Units.KILOMETRE_PER_HOUR);
    System.out.println("The speed of light: " + C);
  }

  @Test
  public void test2() {
    Quantity<Speed> speed = Quantities.getQuantity(60, Units.KILOMETRE_PER_HOUR);
    assertInstanceOf(Integer.class, speed.getValue());
    Quantity<Speed> speed2 = speed.to(Units.METRE_PER_SECOND);
    assertInstanceOf(Double.class, speed2.getValue());
    assertEquals(16.66666, speed2.getValue().doubleValue(), 0.00001);
  }
}