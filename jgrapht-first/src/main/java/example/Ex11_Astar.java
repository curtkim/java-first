package example;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;

import java.util.Arrays;

import static example.Samples.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex11_Astar {

  public static void main(String[] args) {

    AStarShortestPath<Integer, Integer> astar = new AStarShortestPath(Samples.getSample(), (AStarAdmissibleHeuristic<Integer>) (sourceVertex, targetVertex) -> {
      if (!targetVertex.equals(E))
        throw new RuntimeException("");
      return H.get(sourceVertex);
    });

    GraphPath<Integer, Integer> path = astar.getPath(S, E);
    assertEquals(Arrays.asList(S, d, e, E), path.getVertexList());
  }
}
