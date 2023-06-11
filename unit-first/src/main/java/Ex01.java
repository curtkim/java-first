
import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Volume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.units.indriya.unit.Units.METRE;

public class Ex01 {
  public static void main(String[] args) {

    // compilation error
    //capacityMeasure.to(MetricPrefix.MILLI(KILOGRAM));
    Unit<Length> Kilometer = MetricPrefix.KILO(METRE);
    //Unit<Length> Centimeter = MetricPrefix.CENTI(LITRE); // compilation error

    Unit<Length> inch = MetricPrefix.CENTI(METRE).multiply(2.54).asType(Length.class);
    System.out.println(inch);
  }
}
