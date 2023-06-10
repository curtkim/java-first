package example;

import example.adaptor.MyGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.util.Triple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static example.Samples.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex21_MyGraph {

  public static void main(String[] args) {

    MyGraph graph = new MyGraph(7, Arrays.asList(
        Triple.of(S, a, 1.5),
        Triple.of(S, d, 2.0),
        Triple.of(a, S, 1.5),
        Triple.of(a, b, 2.0),
        Triple.of(b, a, 2.0),
        Triple.of(b, c, 3.0),
        Triple.of(c, b, 3.0),
        Triple.of(c, E, 4.0),
        Triple.of(d, S, 2.0),
        Triple.of(d, e, 3.0),
        Triple.of(e, d, 3.0),
        Triple.of(e, E, 2.0)
    ));

    DijkstraShortestPath<Integer, Integer> dijkstra = new DijkstraShortestPath(graph);
    ShortestPathAlgorithm.SingleSourcePaths<Integer, Integer> result = dijkstra.getPaths(S);
    assertEquals(Arrays.asList(S, d, e, E), result.getPath(E).getVertexList());
    assertEquals(7.0, result.getWeight(E), 0.00001);

    // S의 인접 정점들 출력
    // graph.outgoingEdgesOf(S)는 edge의 Set을 반환한다. vertex를 구하기 위해서는 getEdgeTarget()을 사용한다.
    assertEquals(
        new HashSet(Arrays.asList(a, d)),
        graph.outgoingEdgesOf(S).stream().map(e -> graph.getEdgeTarget(e)).collect(Collectors.toSet())
    );
  }
}
