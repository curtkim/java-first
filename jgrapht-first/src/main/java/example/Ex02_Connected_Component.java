package example;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;
import java.util.Set;

public class Ex02_Connected_Component {
  public static void main(String[] args) {
    // constructs a directed graph with the specified vertices and edges
    DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
    g.addVertex("a");
    g.addVertex("b");
    g.addVertex("c");
    g.addVertex("d");
    g.addVertex("e");
    g.addVertex("f");
    g.addVertex("g");
    g.addVertex("h");
    g.addVertex("i");
    DefaultEdge e = g.addEdge("a", "b");
    g.addEdge("b", "d");
    g.addEdge("d", "c");
    g.addEdge("c", "a");
    g.addEdge("e", "d");
    g.addEdge("e", "f");
    g.addEdge("f", "g");
    g.addEdge("g", "e");
    g.addEdge("h", "e");
    g.addEdge("i", "h");

    System.out.println(e.getClass().getName());
    Set<DefaultEdge> edges = g.outgoingEdgesOf("a");
    System.out.println(edges);


    stronglyConnectedSubgraphs(g);
    shortestPath(g);
  }

  static void shortestPath(Graph<String, DefaultEdge> directedGraph) {
    // Prints the shortest path from vertex i to vertex c. This certainly
    // exists for our particular directed graph.
    System.out.println("Shortest path from i to c:");
    DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(directedGraph);
    ShortestPathAlgorithm.SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths("i");
    System.out.println(iPaths.getPath("c") + "\n");

    // Prints the shortest path from vertex c to vertex i. This path does
    // NOT exist for our particular directed graph. Hence the path is
    // empty and the result must be null.
    System.out.println("Shortest path from c to i:");
    ShortestPathAlgorithm.SingleSourcePaths<String, DefaultEdge> cPaths = dijkstraAlg.getPaths("c");
    System.out.println(cPaths.getPath("i"));
  }

  static void stronglyConnectedSubgraphs(Graph<String, DefaultEdge> directedGraph) {
    // computes all the strongly connected components of the directed graph
    StrongConnectivityAlgorithm<String, DefaultEdge> scAlg =
        new KosarajuStrongConnectivityInspector<>(directedGraph);
    List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs = scAlg.getStronglyConnectedComponents();

    // prints the strongly connected components
    System.out.println("Strongly connected components:");
    for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
      System.out.println(stronglyConnectedSubgraphs.get(i));
    }
    System.out.println();
  }
}
