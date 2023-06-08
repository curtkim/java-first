package example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Samples2 {

  public static DefaultDirectedGraph<String, DefaultEdge> getSample(){

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
    return g;
  }
}
