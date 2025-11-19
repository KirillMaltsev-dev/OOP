package ru.nsu.maltsev.Task_1_2_1;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Реализация графа через список смежности.
 * @param <V> тип вершины
 */
public class AdjacencyListGraph<V> implements Graph<V> {
    private final Map<V, List<V>> adjacencyList = new HashMap<>();
    private final boolean directed;

    public AdjacencyListGraph(boolean directed) {
        this.directed = directed;
    }

    @Override
    public boolean addVertex(V vertex) {
        if (adjacencyList.containsKey(vertex)) {
            return false;
        }
        adjacencyList.put(vertex, new ArrayList<>());
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            return false;
        }
        adjacencyList.remove(vertex);
        for (List<V> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
        return true;
    }

    @Override
    public boolean addEdge(V from, V to) {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new IllegalArgumentException("Vertex not found");
        }
        if (adjacencyList.get(from).contains(to)) {
            return false;
        }
        adjacencyList.get(from).add(to);
        if (!directed && !from.equals(to)) {
            adjacencyList.get(to).add(from);
        }
        return true;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            return false;
        }
        boolean removed = adjacencyList.get(from).remove(to);
        if (!directed && !from.equals(to)) {
            adjacencyList.get(to).remove(from);
        }
        return removed;
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            throw new IllegalArgumentException("Vertex not found");
        }
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    @Override
    public Set<V> getVertices() {
        return new HashSet<>(adjacencyList.keySet());
    }

    @Override
    public boolean hasEdge(V from, V to) {
        if (!adjacencyList.containsKey(from)) {
            return false;
        }
        return adjacencyList.get(from).contains(to);
    }

    @Override
    public int getVertexCount() {
        return adjacencyList.size();
    }

    @Override
    public int getEdgeCount() {
        int count = 0;
        for (List<V> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        if (!directed) {
            count /= 2;
        }
        return count;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Adjacency List Graph\n");
        sb.append(directed ? "Directed\n" : "Undirected\n");
        for (V vertex : adjacencyList.keySet()) {
            sb.append(vertex).append(": ");
            sb.append(adjacencyList.get(vertex));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AdjacencyListGraph)) return false;
        AdjacencyListGraph<?> other = (AdjacencyListGraph<?>) obj;
        return directed == other.directed && adjacencyList.equals(other.adjacencyList);
    }

    @Override
    public int hashCode() {
        int result = adjacencyList.hashCode();
        result = 31 * result + Boolean.hashCode(directed);
        return result;
    }
}
