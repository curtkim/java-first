package example;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.Arrays;

import static example.Samples.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex12_Dijkstra {

  public static void main(String[] args) {
    DijkstraShortestPath<Integer, Integer> dijkstra = new DijkstraShortestPath(Samples.getSample());
    ShortestPathAlgorithm.SingleSourcePaths<Integer, Integer> result = dijkstra.getPaths(Samples.S);
    assertEquals(Arrays.asList(S, d, e, E), result.getPath(E).getVertexList());
    assertEquals(7.0, result.getWeight(E), 0.00001);
  }
}
