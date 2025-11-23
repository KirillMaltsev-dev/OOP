package ru.nsu.maltsev.Task_1_2_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit тесты для класса AdjacencyListGraph.
 */
@DisplayName("AdjacencyListGraph Tests")
class AdjacencyListGraphTest {

    private Graph<String> directedGraph;
    private Graph<String> undirectedGraph;

    @BeforeEach
    void setUp() {
        directedGraph = new AdjacencyListGraph<>(true);
        undirectedGraph = new AdjacencyListGraph<>(false);
    }

    @Test
    @DisplayName("Добавление вершины")
    void testAddVertex() {
        assertTrue(directedGraph.addVertex("A"));
        assertFalse(directedGraph.addVertex("A")); // Повторное добавление
        assertEquals(1, directedGraph.getVertexCount());
    }

    @Test
    @DisplayName("Удаление вершины")
    void testRemoveVertex() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        assertTrue(directedGraph.removeVertex("A"));
        assertFalse(directedGraph.removeVertex("A")); // Повторное удаление
        assertEquals(1, directedGraph.getVertexCount());
        assertFalse(directedGraph.hasEdge("A", "B"));
    }

    @Test
    @DisplayName("Добавление ребра в направленный граф")
    void testAddEdgeDirected() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");

        assertTrue(directedGraph.addEdge("A", "B"));
        assertFalse(directedGraph.addEdge("A", "B")); // Повторное добавление
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
        assertFalse(directedGraph.removeEdge("A", "B")); // Повторное удаление
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
            directedGraph.getNeighbors("A");
        });
    }

    @Test
    @DisplayName("Получение всех вершин")
    void testGetVertices() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");

        Set<String> vertices = directedGraph.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains("A"));
        assertTrue(vertices.contains("B"));
        assertTrue(vertices.contains("C"));
    }

    @Test
    @DisplayName("Проверка наличия ребра")
    void testHasEdge() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        assertTrue(directedGraph.hasEdge("A", "B"));
        assertFalse(directedGraph.hasEdge("B", "A"));
        assertFalse(directedGraph.hasEdge("A", "C"));
    }

    @Test
    @DisplayName("Подсчёт вершин и рёбер")
    void testCounts() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addEdge("A", "B");
        directedGraph.addEdge("B", "C");

        assertEquals(3, directedGraph.getVertexCount());
        assertEquals(2, directedGraph.getEdgeCount());
    }

    @Test
    @DisplayName("Проверка типа графа")
    void testIsDirected() {
        assertTrue(directedGraph.isDirected());
        assertFalse(undirectedGraph.isDirected());
    }

    @Test
    @DisplayName("Equals и HashCode")
    void testEqualsAndHashCode() {
        Graph<String> graph1 = new AdjacencyListGraph<>(true);
        Graph<String> graph2 = new AdjacencyListGraph<>(true);

        graph1.addVertex("A");
        graph1.addVertex("B");
        graph1.addEdge("A", "B");

        graph2.addVertex("A");
        graph2.addVertex("B");
        graph2.addEdge("A", "B");

        assertEquals(graph1, graph2);
        assertEquals(graph1.hashCode(), graph2.hashCode());
    }

    @Test
    @DisplayName("ToString содержит информацию о графе")
    void testToString() {
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addEdge("A", "B");

        String str = directedGraph.toString();
        assertTrue(str.contains("Adjacency List Graph"));
        assertTrue(str.contains("Directed"));
        assertTrue(str.contains("A"));
        assertTrue(str.contains("B"));
    }

    @Test
    @DisplayName("Чтение из файла")
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.write(file, List.of(
                "DIRECTED",
                "A B",
                "B C",
                "A C"
        ));

        directedGraph.readFromFile(file.toString());

        assertEquals(3, directedGraph.getVertexCount());
        assertEquals(3, directedGraph.getEdgeCount());
        assertTrue(directedGraph.hasEdge("A", "B"));
        assertTrue(directedGraph.hasEdge("B", "C"));
        assertTrue(directedGraph.hasEdge("A", "C"));
    }

    @Test
    @DisplayName("Чтение из пустого файла")
    void testReadFromEmptyFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, ""); // Полностью пустой файл

        assertThrows(IOException.class, () -> {
            directedGraph.readFromFile(file.toString());
        });
    }
}
