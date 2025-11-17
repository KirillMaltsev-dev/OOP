package ru.nsu.maltsev.Task_1_2_1;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Реализация графа через матрицу смежности.
 * @param <V> тип вершины
 */
public class AdjacencyMatrixGraph<V> implements Graph<V> {
    private final boolean directed;
    private Map<V, Integer> vertexIndices = new HashMap<>();
    private List<V> vertices = new ArrayList<>();
    private boolean[][] adjacencyMatrix = new boolean[0][0];
    private int edgeCount = 0;

    public AdjacencyMatrixGraph(boolean directed) {
        this.directed = directed;
    }

    @Override
    public boolean addVertex(V vertex) {
        if (vertexIndices.containsKey(vertex)) {
            return false;
        }
        vertexIndices.put(vertex, vertices.size());
        vertices.add(vertex);
        // Расширяем матрицу
        int newSize = vertices.size();
        boolean[][] newMatrix = new boolean[newSize][newSize];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix.length);
        }
        adjacencyMatrix = newMatrix;
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Integer idx = vertexIndices.get(vertex);
        if (idx == null) {
            return false;
        }
        // Удаляем вершину и пересобираем всю структуру
        vertices.remove((int) idx);
        vertexIndices.remove(vertex);
        edgeCount = 0;
        // Сдвигаем индексы
        for (int i = 0; i < vertices.size(); i++) {
            vertexIndices.put(vertices.get(i), i);
        }
        // Перестраиваем матрицу
        boolean[][] newMatrix = new boolean[vertices.size()][vertices.size()];
        int oldIdx = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (i == idx) continue;
            int newI = oldIdx++;
            int oldJdx = 0;
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (j == idx) continue;
                int newJ = oldJdx++;
                newMatrix[newI][newJ] = adjacencyMatrix[i][j];
                if (adjacencyMatrix[i][j]) edgeCount++;
            }
        }
        adjacencyMatrix = newMatrix;
        if (!directed) edgeCount /= 2;
        return true;
    }

    @Override
    public boolean addEdge(V from, V to) {
        Integer i = vertexIndices.get(from);
        Integer j = vertexIndices.get(to);
        if (i == null || j == null) {
            throw new IllegalArgumentException("Vertex not found");
        }
        if (adjacencyMatrix[i][j]) {
            return false;
        }
        adjacencyMatrix[i][j] = true;
        if (!directed) {
            adjacencyMatrix[j][i] = true;
        }
        edgeCount++;
        return true;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        Integer i = vertexIndices.get(from);
        Integer j = vertexIndices.get(to);
        if (i == null || j == null) {
            return false;
        }
        if (!adjacencyMatrix[i][j]) {
            return false;
        }
        adjacencyMatrix[i][j] = false;
        if (!directed) {
            adjacencyMatrix[j][i] = false;
        }
        edgeCount--;
        return true;
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        Integer idx = vertexIndices.get(vertex);
        if (idx == null) {
            throw new IllegalArgumentException("Vertex not found");
        }
        List<V> result = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (adjacencyMatrix[idx][j]) {
                result.add(vertices.get(j));
            }
        }
        return result;
    }

    @Override
    public Set<V> getVertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public boolean hasEdge(V from, V to) {
        Integer i = vertexIndices.get(from);
        Integer j = vertexIndices.get(to);
        if (i == null || j == null) {
            return false;
        }
        return adjacencyMatrix[i][j];
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public void readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Empty file");
            }
            // Далее идут вершины и рёбра
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 1) {
                    addVertex((V) parts[0]);
                } else if (parts.length == 2) {
                    if (!vertexIndices.containsKey((V) parts[0])) {
                        addVertex((V) parts[0]);
                    }
                    if (!vertexIndices.containsKey((V) parts[1])) {
                        addVertex((V) parts[1]);
                    }
                    addEdge((V) parts[0], (V) parts[1]);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Adjacency Matrix Graph\n");
        sb.append(directed ? "Directed\n" : "Undirected\n");
        sb.append("    ");
        for (V v : vertices) sb.append(v).append(" ");
        sb.append("\n");
        for (int i = 0; i < vertices.size(); i++) {
            sb.append(vertices.get(i)).append(": ");
            for (int j = 0; j < vertices.size(); j++) {
                sb.append(adjacencyMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AdjacencyMatrixGraph)) return false;
        AdjacencyMatrixGraph<?> other = (AdjacencyMatrixGraph<?>) obj;
        // Сравниваем вершины по порядку и маппингу
        if (!vertices.equals(other.vertices)) return false;
        if (directed != other.directed) return false;
        if (vertices.size() != other.vertices.size()) return false;
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (adjacencyMatrix[i][j] != other.adjacencyMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + Boolean.hashCode(directed);
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                result = 31 * result + (adjacencyMatrix[i][j] ? 1 : 0);
            }
        }
        return result;
    }
}
