package ru.nsu.maltsev.Task_1_2_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit тесты для класса AdjacencyMatrixGraph.
 */
@DisplayName("AdjacencyMatrixGraph Tests")
class AdjacencyMatrixGraphTest {

    private Graph<String> directedGraph;
    private Graph<String> undirectedGraph;

    @BeforeEach
    void setUp() {
        directedGraph = new AdjacencyMatrixGraph<>(true);
        undirectedGraph = new AdjacencyMatrixGraph<>(false);
    }

    @Test
    @DisplayName("Добавление вершины")
    void testAddVertex() {
        assertTrue(directedGraph.addVertex("A"));
        assertFalse(directedGraph.addVertex("A"));
        assertEquals(1, directedGraph.getVertexCount());
    }

    @Test
    @DisplayName("Добавление нескольких вершин")
    void testAddMultipleVertices() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");

        assertEquals(3, directedGraph.getVertexCount());
        assertTrue(directedGraph.getVertices().contains("A"));
        assertTrue(directedGraph.getVertices().contains("B"));
        assertTrue(directedGraph.getVertices().contains("C"));
    }

    @Test
    @DisplayName("Удаление вершины")
    void testRemoveVertex() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addEdge("A", "B");
        directedGraph.addEdge("B", "C");

        assertTrue(directedGraph.removeVertex("B"));
        assertEquals(2, directedGraph.getVertexCount());
        assertEquals(0, directedGraph.getEdgeCount());
        assertFalse(directedGraph.hasEdge("A", "B"));
    }

    @Test
    @DisplayName("Удаление несуществующей вершины")
    void testRemoveNonExistentVertex() {
        assertFalse(directedGraph.removeVertex("Z"));
    }

    @Test
    @DisplayName("Добавление ребра в направленный граф")
    void testAddEdgeDirected() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");

        assertTrue(directedGraph.addEdge("A", "B"));
        assertFalse(directedGraph.addEdge("A", "B"));
        assertTrue(directedGraph.hasEdge("A", "B"));
        assertFalse(directedGraph.hasEdge("B", "A"));
    }

    @Test
    @DisplayName("Добавление ребра в ненаправленный граф")
    void testAddEdgeUndirected() {
        undirectedGraph.addVertex("A");
        undirectedGraph.addVertex("B");

        assertTrue(undirectedGraph.addEdge("A", "B"));
        assertTrue(undirectedGraph.hasEdge("A", "B"));
        assertTrue(undirectedGraph.hasEdge("B", "A"));
        assertEquals(1, undirectedGraph.getEdgeCount());
    }

    @Test
    @DisplayName("Добавление ребра с несуществующей вершиной")
    void testAddEdgeNonExistentVertex() {
        directedGraph.addVertex("A");

        assertThrows(IllegalArgumentException.class, () -> {
            directedGraph.addEdge("A", "B");
        });
    }

    @Test
    @DisplayName("Удаление ребра")
    void testRemoveEdge() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        assertTrue(directedGraph.removeEdge("A", "B"));
        assertFalse(directedGraph.removeEdge("A", "B"));
        assertFalse(directedGraph.hasEdge("A", "B"));
    }

    @Test
    @DisplayName("Удаление ребра в ненаправленном графе")
    void testRemoveEdgeUndirected() {
        undirectedGraph.addVertex("A");
        undirectedGraph.addVertex("B");
        undirectedGraph.addEdge("A", "B");

        assertTrue(undirectedGraph.removeEdge("A", "B"));
        assertFalse(undirectedGraph.hasEdge("A", "B"));
        assertFalse(undirectedGraph.hasEdge("B", "A"));
    }

    @Test
    @DisplayName("Получение соседей")
    void testGetNeighbors() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addEdge("A", "B");
        directedGraph.addEdge("A", "C");

        List<String> neighbors = directedGraph.getNeighbors("A");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("B"));
        assertTrue(neighbors.contains("C"));
    }

    @Test
    @DisplayName("Получение соседей для вершины без рёбер")
    void testGetNeighborsNoEdges() {
        directedGraph.addVertex("A");

        List<String> neighbors = directedGraph.getNeighbors("A");
        assertTrue(neighbors.isEmpty());
    }

    @Test
    @DisplayName("Получение соседей несуществующей вершины")
    void testGetNeighborsNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> {
            directedGraph.getNeighbors("Z");
        });
    }

    @Test
    @DisplayName("Подсчёт рёбер в направленном графе")
    void testEdgeCountDirected() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addEdge("A", "B");
        directedGraph.addEdge("B", "C");
        directedGraph.addEdge("A", "C");

        assertEquals(3, directedGraph.getEdgeCount());
    }

    @Test
    @DisplayName("Подсчёт рёбер в ненаправленном графе")
    void testEdgeCountUndirected() {
        undirectedGraph.addVertex("A");
        undirectedGraph.addVertex("B");
        undirectedGraph.addVertex("C");
        undirectedGraph.addEdge("A", "B");
        undirectedGraph.addEdge("B", "C");

        assertEquals(2, undirectedGraph.getEdgeCount());
    }

    @Test
    @DisplayName("Проверка типа графа")
    void testIsDirected() {
        assertTrue(directedGraph.isDirected());
        assertFalse(undirectedGraph.isDirected());
    }

    @Test
    @DisplayName("Equals для одинаковых графов")
    void testEqualsIdentical() {
        Graph<String> graph1 = new AdjacencyMatrixGraph<>(true);
        Graph<String> graph2 = new AdjacencyMatrixGraph<>(true);

        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B");

        graph2.addVertex("A");
        graph2.addVertex("B");
        graph2.addEdge("A", "B");

        assertEquals(graph1, graph2);
    }

    @Test
    @DisplayName("Equals для разных графов")
    void testEqualsDifferent() {
        Graph<String> graph1 = new AdjacencyMatrixGraph<>(true);
        Graph<String> graph2 = new AdjacencyMatrixGraph<>(true);

        graph1.addVertex("A");
        graph2.addVertex("B");

        assertNotEquals(graph1, graph2);
    }

    @Test
    @DisplayName("ToString содержит информацию")
    void testToString() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        String str = directedGraph.toString();
        assertTrue(str.contains("Adjacency Matrix Graph"));
        assertTrue(str.contains("Directed"));
    }

    @Test
    @DisplayName("Чтение из файла")
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.write(file, List.of(
                "DIRECTED",
                "X Y",
                "Y Z"
        ));

        directedGraph.readFromFile(file.toString());

        assertTrue(directedGraph.getVertexCount() >= 2);
        assertTrue(directedGraph.getEdgeCount() >= 1);
    }

    @Test
    @DisplayName("Проверка расширения матрицы при добавлении вершин")
    void testMatrixExpansion() {
        for (int i = 0; i < 10; i++) {
            directedGraph.addVertex("V" + i);
        }

        assertEquals(10, directedGraph.getVertexCount());
    }
}
