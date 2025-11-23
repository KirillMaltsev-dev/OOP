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
 * Реализация графа через матрицу инцидентности.
 * @param <V> тип вершины
 */
public class IncidenceMatrixGraph<V> implements Graph<V> {
    private final boolean directed;
    private Map<V, Integer> vertexIndices = new HashMap<>();
    private List<V> vertices = new ArrayList<>();
    private List<Edge<V>> edges = new ArrayList<>();
    private int[][] incidenceMatrix = new int[0][0];

    public IncidenceMatrixGraph(boolean directed) {
        this.directed = directed;
    }

    @Override
    public boolean addVertex(V vertex) {
        if (vertexIndices.containsKey(vertex)) {
            return false;
        }
        vertexIndices.put(vertex, vertices.size());
        vertices.add(vertex);
        // Расширяем матрицу по количеству вершин
        int[][] newMatrix = new int[vertices.size()][edges.size()];
        for (int i = 0; i < incidenceMatrix.length; i++) {
            System.arraycopy(incidenceMatrix[i], 0, newMatrix[i], 0, incidenceMatrix[i].length);
        }
        incidenceMatrix = newMatrix;
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Integer idx = vertexIndices.get(vertex);
        if (idx == null) {
            return false;
        }

        // Удаляем вершину
        vertices.remove((int) idx);
        vertexIndices.remove(vertex);

        // Находим все инцидентные рёбра
        Set<Integer> incidentEdges = new HashSet<>();
        if (idx < incidenceMatrix.length) {
            for (int j = 0; j < edges.size(); j++) {
                if (incidenceMatrix[idx][j] != 0) {
                    incidentEdges.add(j);
                }
            }
        }

        // Удаляем инцидентные рёбра
        List<Edge<V>> newEdges = new ArrayList<>();
        for (int j = 0; j < edges.size(); j++) {
            if (!incidentEdges.contains(j)) {
                newEdges.add(edges.get(j));
            }
        }
        edges = newEdges;

        // Пересобираем матрицу без удалённой вершины и рёбер
        int[][] newMatrix = new int[vertices.size()][edges.size()];
        int newRow = 0;
        for (int i = 0; i < incidenceMatrix.length; i++) {
            if (i == idx) continue; // Пропускаем удалённую вершину

            int newCol = 0;
            for (int j = 0; j < incidenceMatrix[i].length; j++) {
                if (!incidentEdges.contains(j)) { // Пропускаем удалённые рёбра
                    if (newCol < edges.size() && newRow < vertices.size()) {
                        newMatrix[newRow][newCol] = incidenceMatrix[i][j];
                    }
                    newCol++;
                }
            }
            newRow++;
        }

        incidenceMatrix = newMatrix;

        // Переиндексируем вершины
        vertexIndices.clear();
        for (int i = 0; i < vertices.size(); i++) {
            vertexIndices.put(vertices.get(i), i);
        }

        return true;
    }


    @Override
    public boolean addEdge(V from, V to) {
        Integer i = vertexIndices.get(from);
        Integer j = vertexIndices.get(to);
        if (i == null || j == null) {
            throw new IllegalArgumentException("Vertex not found");
        }
        // Проверка на дубликат
        for (int k = 0; k < edges.size(); k++) {
            Edge<V> e = edges.get(k);
            if (e.getFrom().equals(from) && e.getTo().equals(to) && e.isDirected() == directed) {
                return false;
            }
            if (!directed && e.getFrom().equals(to) && e.getTo().equals(from)) {
                return false;
            }
        }
        edges.add(new Edge<>(from, to, directed));
        int[][] newMatrix = new int[vertices.size()][edges.size()];
        for (int v = 0; v < vertices.size(); v++) {
            System.arraycopy(incidenceMatrix[v], 0, newMatrix[v], 0, edges.size() - 1);
        }
        if (directed) {
            newMatrix[i][edges.size() - 1] = -1;
            newMatrix[j][edges.size() - 1] = 1;
        } else {
            newMatrix[i][edges.size() - 1] = 1;
            newMatrix[j][edges.size() - 1] = 1;
        }
        incidenceMatrix = newMatrix;
        return true;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        int edgeIndex = -1;
        for (int k = 0; k < edges.size(); k++) {
            Edge<V> e = edges.get(k);
            if (e.getFrom().equals(from) && e.getTo().equals(to) && e.isDirected() == directed) {
                edgeIndex = k;
                break;
            }
            if (!directed && e.getFrom().equals(to) && e.getTo().equals(from)) {
                edgeIndex = k;
                break;
            }
        }
        if (edgeIndex == -1) {
            return false;
        }
        edges.remove(edgeIndex);
        int[][] newMatrix = new int[vertices.size()][edges.size()];
        for (int i = 0; i < vertices.size(); i++) {
            int col = 0;
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                if (j == edgeIndex) continue;
                newMatrix[i][col++] = incidenceMatrix[i][j];
            }
        }
        incidenceMatrix = newMatrix;
        return true;
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        Integer idx = vertexIndices.get(vertex);
        if (idx == null) {
            throw new IllegalArgumentException("Vertex not found");
        }
        Set<V> result = new HashSet<>();
        for (int e = 0; e < edges.size(); e++) {
            if (directed) {
                if (incidenceMatrix[idx][e] == -1) {
                    Edge<V> edge = edges.get(e);
                    result.add(edge.getTo());
                }
            } else {
                if (incidenceMatrix[idx][e] == 1) {
                    Edge<V> edge = edges.get(e);
                    if (!vertex.equals(edge.getFrom())) {
                        result.add(edge.getFrom());
                    }
                    if (!vertex.equals(edge.getTo())) {
                        result.add(edge.getTo());
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

    @Override
    public Set<V> getVertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public boolean hasEdge(V from, V to) {
        for (Edge<V> e : edges) {
            if (e.getFrom().equals(from) && e.getTo().equals(to) && e.isDirected() == directed) {
                return true;
            }
            if (!directed && e.getFrom().equals(to) && e.getTo().equals(from)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
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
            // Далее вершины и рёбра
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
        StringBuilder sb = new StringBuilder("Incidence Matrix Graph\n");
        sb.append(directed ? "Directed\n" : "Undirected\n");
        sb.append("    ");
        for (int e = 0; e < edges.size(); e++) {
            sb.append("E").append(e).append(" ");
        }
        sb.append("\n");
        for (int v = 0; v < vertices.size(); v++) {
            sb.append(vertices.get(v)).append(": ");
            for (int e = 0; e < edges.size(); e++) {
                sb.append(incidenceMatrix[v][e]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IncidenceMatrixGraph)) return false;
        IncidenceMatrixGraph<?> other = (IncidenceMatrixGraph<?>) obj;
        if (!vertices.equals(other.vertices)) return false;
        if (!edges.equals(other.edges)) return false;
        if (directed != other.directed) return false;
        if (incidenceMatrix.length != other.incidenceMatrix.length || incidenceMatrix[0].length != other.incidenceMatrix[0].length) return false;
        for (int i = 0; i < incidenceMatrix.length; i++) {
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                if (incidenceMatrix[i][j] != other.incidenceMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + edges.hashCode();
        result = 31 * result + Boolean.hashCode(directed);
        for (int i = 0; i < incidenceMatrix.length; i++) {
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                result = 31 * result + incidenceMatrix[i][j];
            }
        }
        return result;
    }
}
