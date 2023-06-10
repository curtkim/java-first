package example.adaptor;

import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.alg.util.Triple;
import org.jgrapht.graph.AbstractGraph;
import org.jgrapht.graph.DefaultGraphType;

import java.util.*;
import java.util.function.Supplier;

public class MyGraph extends AbstractGraph<Integer, Integer>{

  protected static final String UNMODIFIABLE = "this graph is unmodifiable";
  protected static final String NO_INCOMING = "this graph does not support incoming edges";


  protected int[] source;
  protected int[] target;
  protected CSRBooleanMatrix outIncidenceMatrix;
  protected double[] weights;

  public MyGraph(int numVertices, List<Triple<Integer, Integer, Double>> edges){
    final int m = edges.size();
    source = new int[m];
    target = new int[m];
    weights = new double[m];

    List<Pair<Integer, Integer>> outgoing = new ArrayList<>(m);
    int eIndex = 0;
    for (Triple<Integer, Integer, Double> e : edges) {
      source[eIndex] = e.getFirst();
      target[eIndex] = e.getSecond();
      outgoing.add(Pair.of(e.getFirst(), eIndex));

      double edgeWeight = e.getThird() != null ? e.getThird() : Graph.DEFAULT_EDGE_WEIGHT;
      weights[eIndex] = edgeWeight;
      eIndex++;
    }

    outIncidenceMatrix = new CSRBooleanMatrix(numVertices, m, outgoing);
  }

  @Override
  public GraphType getType()
  {
    return new DefaultGraphType.Builder()
        .directed()
        .weighted(true)
        .modifiable(false)
        .allowMultipleEdges(false)
        .allowSelfLoops(false)
        .build();
  }

  @Override
  public Supplier<Integer> getVertexSupplier()
  {
    return null;
  }

  @Override
  public Supplier<Integer> getEdgeSupplier()
  {
    return null;
  }

  @Override
  public Integer addEdge(Integer sourceVertex, Integer targetVertex)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public boolean addEdge(Integer sourceVertex, Integer targetVertex, Integer e)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public Integer addVertex()
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public boolean addVertex(Integer v)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public boolean containsEdge(Integer e)
  {
    return e >= 0 && e < outIncidenceMatrix.columns();
  }

  @Override
  public boolean containsVertex(Integer v)
  {
    return v >= 0 && v < outIncidenceMatrix.rows();
  }

  @Override
  public Set<Integer> edgeSet()
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public int degreeOf(Integer vertex)
  {
    throw new UnsupportedOperationException(NO_INCOMING);
  }

  @Override
  public Set<Integer> edgesOf(Integer vertex)
  {
    throw new UnsupportedOperationException(NO_INCOMING);
  }

  @Override
  public int inDegreeOf(Integer vertex)
  {
    throw new UnsupportedOperationException(NO_INCOMING);
  }

  @Override
  public Set<Integer> incomingEdgesOf(Integer vertex)
  {
    throw new UnsupportedOperationException(NO_INCOMING);
  }

  @Override
  public int outDegreeOf(Integer vertex)
  {
    assertVertexExist(vertex);
    return outIncidenceMatrix.nonZeros(vertex);
  }

  @Override
  public Set<Integer> outgoingEdgesOf(Integer vertex)
  {
    assertVertexExist(vertex);
    return outIncidenceMatrix.nonZerosSet(vertex);
  }

  @Override
  public Integer removeEdge(Integer sourceVertex, Integer targetVertex)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public boolean removeEdge(Integer e)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public boolean removeVertex(Integer v)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  @Override
  public Set<Integer> vertexSet()
  {
    return new CompleteIntegerSet(outIncidenceMatrix.rows());
  }

  @Override
  public Integer getEdgeSource(Integer e)
  {
    assertEdgeExist(e);
    return source[e];
  }

  @Override
  public Integer getEdgeTarget(Integer e)
  {
    assertEdgeExist(e);
    return target[e];
  }


  @Override
  public double getEdgeWeight(Integer e)
  {
    assertEdgeExist(e);
    return weights[e];
  }

  @Override
  public void setEdgeWeight(Integer e, double weight)
  {
    throw new UnsupportedOperationException(UNMODIFIABLE);
  }

  /**
   * {@inheritDoc}
   *
   * This operation costs $O(d)$ where $d$ is the out-degree of the source vertex.
   */
  @Override
  public Integer getEdge(Integer sourceVertex, Integer targetVertex)
  {
    if (sourceVertex < 0 || sourceVertex >= outIncidenceMatrix.rows()) {
      return null;
    }
    if (targetVertex < 0 || targetVertex >= outIncidenceMatrix.rows()) {
      return null;
    }

    Iterator<Integer> it = outIncidenceMatrix.nonZerosPositionIterator(sourceVertex);
    while (it.hasNext()) {
      int eId = it.next();
      if (getEdgeTarget(eId) == targetVertex) {
        return eId;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   *
   * This operation costs $O(d)$ where $d$ is the out-degree of the source vertex.
   */
  @Override
  public Set<Integer> getAllEdges(Integer sourceVertex, Integer targetVertex)
  {
    if (sourceVertex < 0 || sourceVertex >= outIncidenceMatrix.rows()) {
      return null;
    }
    if (targetVertex < 0 || targetVertex >= outIncidenceMatrix.rows()) {
      return null;
    }

    Set<Integer> result = new LinkedHashSet<>();

    Iterator<Integer> it = outIncidenceMatrix.nonZerosPositionIterator(sourceVertex);
    while (it.hasNext()) {
      int eId = it.next();

      if (getEdgeTarget(eId) == targetVertex) {
        result.add(eId);
      }
    }
    return result;
  }

  /**
   * Ensures that the specified vertex exists in this graph, or else throws exception.
   *
   * @param v vertex
   * @return <code>true</code> if this assertion holds.
   * @throws IllegalArgumentException if specified vertex does not exist in this graph.
   */
  protected boolean assertVertexExist(Integer v)
  {
    if (v >= 0 && v < outIncidenceMatrix.rows()) {
      return true;
    } else {
      throw new IllegalArgumentException("no such vertex in graph: " + v.toString());
    }
  }

  /**
   * Ensures that the specified edge exists in this graph, or else throws exception.
   *
   * @param e edge
   * @return <code>true</code> if this assertion holds.
   * @throws IllegalArgumentException if specified edge does not exist in this graph.
   */
  protected boolean assertEdgeExist(Integer e)
  {
    if (e >= 0 && e < outIncidenceMatrix.columns()) {
      return true;
    } else {
      throw new IllegalArgumentException("no such edge in graph: " + e.toString());
    }
  }
}
