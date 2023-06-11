import org.junit.jupiter.api.Test;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.quantity.Quantities;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Area;
import javax.measure.quantity.Length;
import javax.measure.quantity.Volume;
import javax.measure.quantity.Speed;


import static org.junit.jupiter.api.Assertions.*;
import static tech.units.indriya.unit.Units.*;

// Unit
// Quantity
public class MeasureTest {

  @Test
  public void givenQuantity_whenGetUnitAndConvertValue_thenSuccess() {
    Quantity<Volume> capacityMeasure = Quantities.getQuantity(9.2, LITRE);
    assert LITRE == capacityMeasure.getUnit();
    assertInstanceOf(Double.class, capacityMeasure.getValue());

    double volumeInLitre = capacityMeasure.getValue().doubleValue();
    assertEquals(9.2, volumeInLitre, 0.0f);

    double volumeInMilliLitre = capacityMeasure.to(MetricPrefix.MILLI(LITRE)).getValue().doubleValue();
    assertEquals(9200.0, volumeInMilliLitre, 0.0f);
  }

  @Test
  public void conversion() {
    Unit<Length> Kilometer = MetricPrefix.KILO(METRE);
    Unit<Length> Centimeter = MetricPrefix.CENTI(METRE);
  }

  @Test
  public void givenUnit_whenProduct_ThenGetProductUnit() {
    Unit<Area> squareMetre = METRE.multiply(METRE).asType(Area.class);
    Quantity<Length> line = Quantities.getQuantity(2, METRE);
    assertEquals(line.multiply(line).getUnit(), squareMetre);
    assertEquals(4, line.multiply(line).getValue());
  }

  @Test
  public void givenMeters_whenConvertToKilometer_ThenConverted() {
    // 잉 int -> double
    int distanceInMeters = 50;
    UnitConverter metreToKilometre = METRE.getConverterTo(MetricPrefix.KILO(METRE));
    double distanceInKilometers = metreToKilometre.convert(distanceInMeters);
    assertEquals(0.05, distanceInKilometers, 0.00f);
  }

  @Test
  public void givenSymbol_WhenCompareToSystemUnit_ThenSuccess() {
    assertEquals(SimpleUnitFormat.getInstance().parse("kW"), MetricPrefix.KILO(WATT));
    assertEquals(SimpleUnitFormat.getInstance().parse("ms"), SECOND.divide(1000));
  }

  @Test
  public void givenUnits_WhenAdd_ThenSuccess() {
    Quantity<Length> total = Quantities.getQuantity(2, METRE).add(Quantities.getQuantity(3, METRE));
    assertEquals(total.getValue().intValue(), 5);
    assertInstanceOf(Integer.class, total.getValue());

    Quantity<Length> totalLength =
        Quantities.getQuantity(2, METRE).add(Quantities.getQuantity(3, MetricPrefix.KILO(METRE)));
    assertEquals(totalLength.getValue().intValue(), 3002);
    assertEquals(METRE, totalLength.getUnit());
    assertInstanceOf(Integer.class, totalLength.getValue());
  }
}
