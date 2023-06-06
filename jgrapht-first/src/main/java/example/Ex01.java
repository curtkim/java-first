package example;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Ex01 {

  public static void main(String[] args) throws URISyntaxException {
    Graph<URI, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

    URI google = new URI("http://www.google.com");
    URI wikipedia = new URI("http://www.wikipedia.org");
    URI jgrapht = new URI("http://www.jgrapht.org");

    // add the vertices
    g.addVertex(google);
    g.addVertex(wikipedia);
    g.addVertex(jgrapht);

    // add edges to create linking structure
    g.addEdge(jgrapht, wikipedia);
    g.addEdge(google, jgrapht);
    g.addEdge(google, wikipedia);
    g.addEdge(wikipedia, google);

    URI start = g.vertexSet().stream().filter(uri -> uri.getHost().equals("www.jgrapht.org")).findAny()
        .get();
    System.out.println(start);

    System.out.println("depth first iterator");
    Iterator<URI> iterator = new DepthFirstIterator<>(g, start);
    while (iterator.hasNext()) {
      URI uri = iterator.next();
      System.out.println(uri);
    }
  }
}
