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
    DefaultDirectedGraph<String, DefaultEdge> g = Samples2.getSample();

    Set<DefaultEdge> edges = g.outgoingEdgesOf("a");
    System.out.println(edges);

    stronglyConnectedSubgraphs(g);
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
