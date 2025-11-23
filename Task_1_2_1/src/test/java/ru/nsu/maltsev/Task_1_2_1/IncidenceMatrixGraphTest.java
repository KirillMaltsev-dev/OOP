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
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit тесты для класса IncidenceMatrixGraph.
 */
@DisplayName("IncidenceMatrixGraph Tests")
class IncidenceMatrixGraphTest {

    private Graph<String> directedGraph;
    private Graph<String> undirectedGraph;

    @BeforeEach
    void setUp() {
        directedGraph = new IncidenceMatrixGraph<>(true);
        undirectedGraph = new IncidenceMatrixGraph<>(false);
    }

    @Test
    @DisplayName("Добавление вершины")
    void testAddVertex() {
        assertTrue(directedGraph.addVertex("A"));
        assertFalse(directedGraph.addVertex("A"));
        assertEquals(1, directedGraph.getVertexCount());
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
        assertFalse(directedGraph.hasEdge("A", "B"));
    }

    @Test
    @DisplayName("Добавление ребра в направленный граф")
    void testAddEdgeDirected() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");

        assertTrue(directedGraph.addEdge("A", "B"));
        assertFalse(directedGraph.addEdge("A", "B"));
        assertEquals(1, directedGraph.getEdgeCount());
        assertTrue(directedGraph.hasEdge("A", "B"));
        assertFalse(directedGraph.hasEdge("B", "A"));
    }

    @Test
    @DisplayName("Добавление ребра в ненаправленный граф")
    void testAddEdgeUndirected() {
        undirectedGraph.addVertex("A");
        undirectedGraph.addVertex("B");

        assertTrue(undirectedGraph.addEdge("A", "B"));
        assertEquals(1, undirectedGraph.getEdgeCount());
        assertTrue(undirectedGraph.hasEdge("A", "B"));
        assertTrue(undirectedGraph.hasEdge("B", "A"));
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
        assertEquals(0, directedGraph.getEdgeCount());
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
    @DisplayName("Получение соседей несуществующей вершины")
    void testGetNeighborsNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> {
            directedGraph.getNeighbors("Z");
        });
    }

    @Test
    @DisplayName("Подсчёт вершин")
    void testVertexCount() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");

        assertEquals(3, directedGraph.getVertexCount());
    }

    @Test
    @DisplayName("Подсчёт рёбер")
    void testEdgeCount() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addEdge("A", "B");
        directedGraph.addEdge("B", "C");

        assertEquals(2, directedGraph.getEdgeCount());
    }

    @Test
    @DisplayName("Проверка наличия ребра")
    void testHasEdge() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        assertTrue(directedGraph.hasEdge("A", "B"));
        assertFalse(directedGraph.hasEdge("B", "A"));
    }

    @Test
    @DisplayName("Проверка типа графа")
    void testIsDirected() {
        assertTrue(directedGraph.isDirected());
        assertFalse(undirectedGraph.isDirected());
    }

    @Test
    @DisplayName("Equals для одинаковых графов")
    void testEquals() {
        Graph<String> graph1 = new IncidenceMatrixGraph<>(true);
        Graph<String> graph2 = new IncidenceMatrixGraph<>(true);

        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B");

        graph2.addVertex("A");
        graph2.addVertex("B");
        graph2.addEdge("A", "B");

        assertEquals(graph1, graph2);
    }

    @Test
    @DisplayName("ToString содержит информацию")
    void testToString() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        String str = directedGraph.toString();
        assertTrue(str.contains("Incidence Matrix Graph"));
        assertTrue(str.contains("Directed"));
    }

    @Test
    @DisplayName("Чтение из файла")
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.write(file, List.of(
                "DIRECTED",
                "A B",
                "B C"
        ));

        directedGraph.readFromFile(file.toString());
        assertTrue(directedGraph.getVertexCount() >= 2);
    }

    @Test
    @DisplayName("Множественные рёбра")
    void testMultipleEdges() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addVertex("D");

        directedGraph.addEdge("A", "B");
        directedGraph.addEdge("A", "C");
        directedGraph.addEdge("B", "D");
        directedGraph.addEdge("C", "D");

        assertEquals(4, directedGraph.getEdgeCount());
    }
}
