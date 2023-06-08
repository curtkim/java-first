package example;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgrapht.GraphType;
import org.jgrapht.alg.util.Pair;
//import org.jgrapht.opt.graph.sparse.IncomingEdgesSupport;
import org.jgrapht.opt.graph.sparse.SparseIntDirectedGraph;

import static org.junit.jupiter.api.Assertions.*;

public class Ex03_SparseIntGraph {

  static Set<Integer> Setof(int... values) {
    return IntStream.of(values).boxed().collect(Collectors.toSet());
  }

  public static void main(String[] args){
    final int vertexCount = 8;
    List<Pair<Integer, Integer>> edges = Arrays
        .asList(
            Pair.of(0, 1), Pair.of(1, 0), Pair.of(1, 4), Pair.of(1, 5), Pair.of(1, 6),
            Pair.of(2, 4), Pair.of(3, 4), Pair.of(4, 5),
            Pair.of(5, 6), Pair.of(7, 6), Pair.of(7, 7));

    SparseIntDirectedGraph g= new SparseIntDirectedGraph(vertexCount, edges);//, IncomingEdgesSupport.NO_INCOMING_EDGES);


    assertEquals(vertexCount, g.vertexSet().size());
    assertEquals(edges.size(), g.edgeSet().size());

    assertEquals(
        IntStream.range(0, edges.size()).mapToObj(Integer::valueOf).collect(Collectors.toSet()),
        g.edgeSet());
    assertEquals(
        IntStream.range(0, vertexCount).mapToObj(Integer::valueOf).collect(Collectors.toSet()),
        g.vertexSet());

    for (int i = 0; i < vertexCount; i++) {
      assertTrue(g.containsVertex(i));
    }

    assertEquals(1, g.outDegreeOf(0));
    assertEquals(Setof(0), g.outgoingEdgesOf(0));
    //
    assertEquals(0, g.getEdgeSource(0));
    assertEquals(1, g.getEdgeTarget(0));

    assertEquals(5, g.degreeOf(1));
    assertEquals(1, g.inDegreeOf(1));
    assertEquals(4, g.outDegreeOf(1));
    assertEquals(Setof(0, 1, 2, 3, 4), g.edgesOf(1));
    assertEquals(Setof(0), g.incomingEdgesOf(1));
    assertEquals(Setof(1, 2, 3, 4), g.outgoingEdgesOf(1));

    assertEquals(3, g.degreeOf(2));
    assertEquals(0, g.inDegreeOf(2));
    assertEquals(3, g.outDegreeOf(2));
    assertEquals(Setof(5, 6, 7), g.edgesOf(2));
    assertEquals(Setof(), g.incomingEdgesOf(2));
    assertEquals(Setof(5, 6, 7), g.outgoingEdgesOf(2));

    assertEquals(1, g.degreeOf(3));
    assertEquals(0, g.inDegreeOf(3));
    assertEquals(1, g.outDegreeOf(3));
    assertEquals(Setof(8), g.edgesOf(3));
    assertEquals(Setof(), g.incomingEdgesOf(3));
    assertEquals(Setof(8), g.outgoingEdgesOf(3));

    assertEquals(6, g.degreeOf(4));
    assertEquals(5, g.inDegreeOf(4));
    assertEquals(1, g.outDegreeOf(4));
    assertEquals(Setof(2, 5, 6, 7, 8, 9), g.edgesOf(4));
    assertEquals(Setof(2, 5, 6, 7, 8), g.incomingEdgesOf(4));
    assertEquals(Setof(9), g.outgoingEdgesOf(4));

    assertEquals(3, g.degreeOf(5));
    assertEquals(2, g.inDegreeOf(5));
    assertEquals(1, g.outDegreeOf(5));
    assertEquals(Setof(3, 9, 10), g.edgesOf(5));
    assertEquals(Setof(3, 9), g.incomingEdgesOf(5));
    assertEquals(Setof(10), g.outgoingEdgesOf(5));

    assertEquals(3, g.degreeOf(6));
    assertEquals(3, g.inDegreeOf(6));
    assertEquals(0, g.outDegreeOf(6));
    assertEquals(Setof(4, 10, 11), g.edgesOf(6));
    assertEquals(Setof(4, 10, 11), g.incomingEdgesOf(6));
    assertEquals(Setof(), g.outgoingEdgesOf(6));

    assertEquals(3, g.degreeOf(7));
    assertEquals(1, g.inDegreeOf(7));
    assertEquals(2, g.outDegreeOf(7));
    assertEquals(Setof(11, 12), g.edgesOf(7));
    assertEquals(Setof(12), g.incomingEdgesOf(7));
    assertEquals(Setof(11, 12), g.outgoingEdgesOf(7));

    assertEquals(Integer.valueOf(0), g.getEdgeSource(0));
    assertEquals(Integer.valueOf(1), g.getEdgeTarget(0));
    assertEquals(Integer.valueOf(1), g.getEdgeSource(1));
    assertEquals(Integer.valueOf(0), g.getEdgeTarget(1));
    assertEquals(Integer.valueOf(1), g.getEdgeSource(2));
    assertEquals(Integer.valueOf(4), g.getEdgeTarget(2));
    assertEquals(Integer.valueOf(1), g.getEdgeSource(3));
    assertEquals(Integer.valueOf(5), g.getEdgeTarget(3));
    assertEquals(Integer.valueOf(1), g.getEdgeSource(4));
    assertEquals(Integer.valueOf(6), g.getEdgeTarget(4));
    assertEquals(Integer.valueOf(2), g.getEdgeSource(5));
    assertEquals(Integer.valueOf(4), g.getEdgeTarget(5));
    assertEquals(Integer.valueOf(2), g.getEdgeSource(6));
    assertEquals(Integer.valueOf(4), g.getEdgeTarget(6));
    assertEquals(Integer.valueOf(2), g.getEdgeSource(7));
    assertEquals(Integer.valueOf(4), g.getEdgeTarget(7));
    assertEquals(Integer.valueOf(3), g.getEdgeSource(8));
    assertEquals(Integer.valueOf(4), g.getEdgeTarget(8));
    assertEquals(Integer.valueOf(4), g.getEdgeSource(9));
    assertEquals(Integer.valueOf(5), g.getEdgeTarget(9));
    assertEquals(Integer.valueOf(5), g.getEdgeSource(10));
    assertEquals(Integer.valueOf(6), g.getEdgeTarget(10));
    assertEquals(Integer.valueOf(7), g.getEdgeSource(11));
    assertEquals(Integer.valueOf(6), g.getEdgeTarget(11));
    assertEquals(Integer.valueOf(7), g.getEdgeSource(12));
    assertEquals(Integer.valueOf(7), g.getEdgeTarget(12));

    GraphType type = g.getType();
    assertTrue(type.isAllowingCycles());
    assertTrue(type.isAllowingMultipleEdges());
    assertTrue(type.isAllowingSelfLoops());
    assertTrue(type.isDirected());
    assertFalse(type.isModifiable());
    assertFalse(type.isUndirected());
    assertFalse(type.isMixed());
    assertFalse(type.isWeighted());

    assertEquals(Integer.valueOf(0), g.getEdge(0, 1));
    assertEquals(Set.of(0), g.getAllEdges(0, 1));
    assertEquals(Integer.valueOf(1), g.getEdge(1, 0));
    assertEquals(Set.of(1), g.getAllEdges(1, 0));
    assertEquals(Integer.valueOf(2), g.getEdge(1, 4));
    assertEquals(Set.of(2), g.getAllEdges(1, 4));
    assertEquals(Integer.valueOf(3), g.getEdge(1, 5));
    assertEquals(Set.of(3), g.getAllEdges(1, 5));
    assertEquals(Integer.valueOf(4), g.getEdge(1, 6));
    assertEquals(Set.of(4), g.getAllEdges(1, 6));
    assertEquals(Integer.valueOf(5), g.getEdge(2, 4));
    assertEquals(Set.of(5, 6, 7), g.getAllEdges(2, 4));
    assertEquals(Integer.valueOf(8), g.getEdge(3, 4));
    assertEquals(Set.of(8), g.getAllEdges(3, 4));
    assertEquals(Integer.valueOf(9), g.getEdge(4, 5));
    assertEquals(Set.of(9), g.getAllEdges(4, 5));
    assertEquals(Integer.valueOf(10), g.getEdge(5, 6));
    assertEquals(Set.of(10), g.getAllEdges(5, 6));
    assertEquals(Integer.valueOf(11), g.getEdge(7, 6));
    assertEquals(Set.of(11), g.getAllEdges(7, 6));
    assertEquals(Integer.valueOf(12), g.getEdge(7, 7));
    assertEquals(Set.of(12), g.getAllEdges(7, 7));
  }
}
