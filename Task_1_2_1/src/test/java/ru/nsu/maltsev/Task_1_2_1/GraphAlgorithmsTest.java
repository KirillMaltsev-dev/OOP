package ru.nsu.maltsev.Task_1_2_1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit тесты для класса GraphAlgorithms.
 */
@DisplayName("GraphAlgorithms Tests")
class GraphAlgorithmsTest {

    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph<>(true);
    }

    @Test
    @DisplayName("Топологическая сортировка простого DAG")
    void testTopologicalSortSimpleDAG() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertEquals(4, sorted.size());
        assertTrue(sorted.indexOf("A") < sorted.indexOf("B"));
        assertTrue(sorted.indexOf("A") < sorted.indexOf("C"));
        assertTrue(sorted.indexOf("B") < sorted.indexOf("D"));
        assertTrue(sorted.indexOf("C") < sorted.indexOf("D"));
    }

    @Test
    @DisplayName("Топологическая сортировка линейного графа")
    void testTopologicalSortLinear() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertEquals(List.of("A", "B", "C"), sorted);
    }

    @Test
    @DisplayName("Топологическая сортировка графа с одной вершиной")
    void testTopologicalSortSingleVertex() {
        graph.addVertex("A");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertEquals(1, sorted.size());
        assertEquals("A", sorted.get(0));
    }

    @Test
    @DisplayName("Топологическая сортировка пустого графа")
    void testTopologicalSortEmptyGraph() {
        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertTrue(sorted.isEmpty());
    }

    @Test
    @DisplayName("Топологическая сортировка графа без рёбер")
    void testTopologicalSortNoEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertEquals(3, sorted.size());
        assertTrue(sorted.contains("A"));
        assertTrue(sorted.contains("B"));
        assertTrue(sorted.contains("C"));
    }

    @Test
    @DisplayName("Обнаружение простого цикла")
    void testCycleDetectionSimple() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            GraphAlgorithms.topologicalSort(graph);
        });

        assertTrue(exception.getMessage().contains("cycle"));
    }

    @Test
    @DisplayName("Обнаружение самоцикла")
    void testCycleDetectionSelfLoop() {
        graph.addVertex("A");
        graph.addEdge("A", "A");

        assertThrows(IllegalStateException.class, () -> {
            GraphAlgorithms.topologicalSort(graph);
        });
    }

    @Test
    @DisplayName("Обнаружение цикла в сложном графе")
    void testCycleDetectionComplex() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "B"); // Цикл B -> C -> D -> B

        assertThrows(IllegalStateException.class, () -> {
            GraphAlgorithms.topologicalSort(graph);
        });
    }

    @Test
    @DisplayName("Исключение для неориентированного графа")
    void testUndirectedGraphException() {
        Graph<String> undirected = new AdjacencyListGraph<>(false);
        undirected.addVertex("A");
        undirected.addVertex("B");
        undirected.addEdge("A", "B");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            GraphAlgorithms.topologicalSort(undirected);
        });

        assertTrue(exception.getMessage().contains("directed"));
    }

    @Test
    @DisplayName("Топологическая сортировка с разными реализациями графа")
    void testTopologicalSortDifferentImplementations() {
        Graph<String> listGraph = new AdjacencyListGraph<>(true);
        Graph<String> matrixGraph = new AdjacencyMatrixGraph<>(true);
        Graph<String> incidenceGraph = new IncidenceMatrixGraph<>(true);

        for (Graph<String> g : new Graph[]{listGraph, matrixGraph, incidenceGraph}) {
            g.addVertex("A");
            g.addVertex("B");
            g.addVertex("C");
            g.addEdge("A", "B");
            g.addEdge("B", "C");

            List<String> sorted = GraphAlgorithms.topologicalSort(g);
            assertEquals(List.of("A", "B", "C"), sorted);
        }
    }

    @Test
    @DisplayName("Топологическая сортировка графа с несколькими компонентами связности")
    void testTopologicalSortDisconnected() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertEquals(4, sorted.size());
        assertTrue(sorted.indexOf("A") < sorted.indexOf("B"));
        assertTrue(sorted.indexOf("C") < sorted.indexOf("D"));
    }

    @Test
    @DisplayName("Топологическая сортировка сложного DAG")
    void testTopologicalSortComplexDAG() {
        // Граф зависимостей задач
        graph.addVertex("Task1");
        graph.addVertex("Task2");
        graph.addVertex("Task3");
        graph.addVertex("Task4");
        graph.addVertex("Task5");

        graph.addEdge("Task1", "Task2");
        graph.addEdge("Task1", "Task3");
        graph.addEdge("Task2", "Task4");
        graph.addEdge("Task3", "Task4");
        graph.addEdge("Task4", "Task5");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);

        assertEquals(5, sorted.size());
        assertEquals("Task1", sorted.get(0));
        assertEquals("Task5", sorted.get(4));
        assertTrue(sorted.indexOf("Task2") < sorted.indexOf("Task4"));
        assertTrue(sorted.indexOf("Task3") < sorted.indexOf("Task4"));
    }

    @Test
    @DisplayName("Топологическая сортировка с числовыми вершинами")
    void testTopologicalSortWithIntegers() {
        Graph<Integer> intGraph = new AdjacencyListGraph<>(true);
        intGraph.addVertex(1);
        intGraph.addVertex(2);
        intGraph.addVertex(3);
        intGraph.addEdge(1, 2);
        intGraph.addEdge(2, 3);

        List<Integer> sorted = GraphAlgorithms.topologicalSort(intGraph);

        assertEquals(List.of(1, 2, 3), sorted);
    }
}
