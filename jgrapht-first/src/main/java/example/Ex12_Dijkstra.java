package example;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.opt.graph.sparse.SparseIntDirectedWeightedGraph;

import java.util.Arrays;

import static example.Samples.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex12_Dijkstra {

  public static void main(String[] args) {
    SparseIntDirectedWeightedGraph g = Samples.getSample();

    DijkstraShortestPath<Integer, Integer> dijkstra = new DijkstraShortestPath(g);
    ShortestPathAlgorithm.SingleSourcePaths<Integer, Integer> result = dijkstra.getPaths(Samples.S);
    assertEquals(Arrays.asList(S, d, e, E), result.getPath(E).getVertexList());
    assertEquals(7.0, result.getWeight(E), 0.00001);

    // S의 인접 정점들 출력
    g.outgoingEdgesOf(S).stream().map(e -> g.getEdgeTarget(e)).forEach(System.out::println);

  }
}
