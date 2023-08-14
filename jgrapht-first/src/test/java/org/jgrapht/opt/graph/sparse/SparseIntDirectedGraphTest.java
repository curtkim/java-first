package org.jgrapht.opt.graph.sparse;

import org.jgrapht.alg.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SparseIntDirectedGraphTest {

  @Test
  public void test(){
    final int vertexCount = 8;
    List<Pair<Integer, Integer>> edges = Arrays
        .asList(
            Pair.of(0, 1),
            Pair.of(1, 0), Pair.of(1, 4), Pair.of(1, 5), Pair.of(1, 6),
            Pair.of(2, 4),
            Pair.of(3, 4),
            Pair.of(4, 5),
            Pair.of(5, 6),
            Pair.of(7, 6), Pair.of(7, 7)
        );

    SparseIntDirectedGraph g= new SparseIntDirectedGraph(vertexCount, edges);//, IncomingEdgesSupport.NO_INCOMING_EDGES);

    assertEquals(vertexCount, g.vertexSet().size());
    assertEquals(edges.size(), g.edgeSet().size());
    //System.out.println(g.edgeSet());

    // 0->1로 가는 edge는 0번째 edge이다.
    assertEquals(Set.of(0), g.outgoingEdgesOf(0));
    assertEquals(0, g.getEdge(0, 1));
    assertEquals(0, g.getEdgeSource(0));
    assertEquals(1, g.getEdgeTarget(0));


    // 1에서 나가는 edge는 1,2,3,4
    assertEquals(Set.of(1, 2, 3, 4), g.outgoingEdgesOf(1));
    // 1->4 edge는 2번이고
    assertEquals(2, g.getEdge(1, 4));
    assertEquals(1, g.getEdgeSource(2));
    assertEquals(4, g.getEdgeTarget(2));
  }
}
