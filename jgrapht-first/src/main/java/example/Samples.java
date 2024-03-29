package example;

import org.jgrapht.alg.util.Triple;
//import org.jgrapht.opt.graph.sparse.IncomingEdgesSupport;
import org.jgrapht.opt.graph.sparse.SparseIntDirectedWeightedGraph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Samples {
  public static int S = 0;
  public static int a = 1;
  public static int b = 2;
  public static int c = 3;
  public static int d = 4;
  public static int e = 5;
  public static int E = 6;

  // E까지의 직선거리, Astar에서 휴리스틱으로 사용
  public static Map<Integer, Double> H;
  static {
    H = new HashMap<>();
    H.put(S, 6.0);
    H.put(a, 4.0);
    H.put(b, 2.0);
    H.put(c, 4.0);
    H.put(d, 4.5);
    H.put(e, 2.0);
    H.put(E, 0.0);
  }

  public static SparseIntDirectedWeightedGraph getSample(){

    // from https://upload.wikimedia.org/wikipedia/commons/9/98/AstarExampleEn.gif

    final int vertexCount = 7;
    List<Triple<Integer, Integer, Double>> edges = Arrays.asList(
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
    );
    SparseIntDirectedWeightedGraph g = new SparseIntDirectedWeightedGraph(vertexCount, edges);
    return g;
  }
}
