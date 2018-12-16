package tutorial.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.springframework.data.geo.Point;

public class PointDeserializer extends StdDeserializer<Point> {

  public PointDeserializer() {
    this(null);
  }

  public PointDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Point deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);

    double x = node.get("x").asDouble();
    double y = node.get("y").asDouble();
    return new Point(x, y);
  }
}
