package tutorial;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.geo.Point;
import tutorial.io.PointDeserializer;

public class ObjectMapperTest {

  static double DELTA = 0.000000001;

  ObjectMapper objectMapper = new ObjectMapper();

  @Before
  public void before(){
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Point.class, new PointDeserializer());
    objectMapper.registerModule(module);
  }

  @Test
  public void test() throws IOException {
    Pet pet = new Pet("1", "Liam", "cat", "siamese", 5.3, new Point(1.1, 2.2));
    assertThat(objectMapper.writeValueAsString(pet), containsString("foot_size"));

    ObjectReader reader = objectMapper.reader().forType(Pet.class);

    Pet pet2 = reader.readValue(objectMapper.writeValueAsString(pet));
    assertEquals(5.3, pet2.footSize, DELTA);
  }

}
