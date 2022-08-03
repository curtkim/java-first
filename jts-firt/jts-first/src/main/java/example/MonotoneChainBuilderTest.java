package example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.index.chain.MonotoneChain;
import org.locationtech.jts.index.chain.MonotoneChainBuilder;

import java.util.ArrayList;
import java.util.List;

public class MonotoneChainBuilderTest {

  public static String toString(MonotoneChain chain){
    return String.format("id=%d, start=%d, end=%d, env=%s",
        chain.getId(), chain.getStartIndex(), chain.getEndIndex(), chain.getEnvelope());
  }

  public static void main(String[] args) {
    /*
    Coordinate[] coords = new Coordinate[]{
        new Coordinate(0,0),
        new Coordinate(1, 0),
        new Coordinate(0.5, 0),
        new Coordinate(2, 0)
    };

    List<MonotoneChain> list = MonotoneChainBuilder.getChains(coords);
    for(MonotoneChain chain : list){
      System.out.println(toString(chain));
    }
*/

    Coordinate[] coords2 = new Coordinate[]{
        new Coordinate(0,0),
        new Coordinate(1, 0),
        new Coordinate(1, 1),
        new Coordinate(0, 1),
        new Coordinate(0, 0)
    };

    List<MonotoneChain> list2= MonotoneChainBuilder.getChains(coords2);
    for(MonotoneChain chain : list2){
      System.out.println(toString(chain));
    }
  }
}
