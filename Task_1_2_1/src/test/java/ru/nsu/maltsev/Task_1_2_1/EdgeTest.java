package ru.nsu.maltsev.Task_1_2_1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit тесты для класса Edge.
 */
@DisplayName("Edge Class Tests")
class EdgeTest {

    @Test
    @DisplayName("Создание направленного ребра")
    void testDirectedEdgeCreation() {
        Edge<String> edge = new Edge<>("A", "B", true);

        assertEquals("A", edge.getFrom());
        assertEquals("B", edge.getTo());
        assertTrue(edge.isDirected());
    }

    @Test
    @DisplayName("Создание ненаправленного ребра")
    void testUndirectedEdgeCreation() {
        Edge<Integer> edge = new Edge<>(1, 2, false);

        assertEquals(1, edge.getFrom());
        assertEquals(2, edge.getTo());
        assertFalse(edge.isDirected());
    }

    @Test
    @DisplayName("Equals для направленных рёбер - одинаковое направление")
    void testDirectedEdgeEqualsSameDirection() {
        Edge<String> edge1 = new Edge<>("A", "B", true);
        Edge<String> edge2 = new Edge<>("A", "B", true);

        assertEquals(edge1, edge2);
        assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    @DisplayName("Equals для направленных рёбер - разное направление")
    void testDirectedEdgeEqualsDifferentDirection() {
        Edge<String> edge1 = new Edge<>("A", "B", true);
        Edge<String> edge2 = new Edge<>("B", "A", true);

        assertNotEquals(edge1, edge2);
    }

    @Test
    @DisplayName("Equals для ненаправленных рёбер - любое направление")
    void testUndirectedEdgeEqualsAnyDirection() {
        Edge<String> edge1 = new Edge<>("A", "B", false);
        Edge<String> edge2 = new Edge<>("B", "A", false);

        assertEquals(edge1, edge2);
    }

    @Test
    @DisplayName("Equals с самим собой")
    void testEdgeEqualsSelf() {
        Edge<String> edge = new Edge<>("A", "B", true);

        assertEquals(edge, edge);
    }

    @Test
    @DisplayName("Equals с null")
    void testEdgeEqualsNull() {
        Edge<String> edge = new Edge<>("A", "B", true);

        assertNotEquals(null, edge);
    }

    @Test
    @DisplayName("Equals с объектом другого класса")
    void testEdgeEqualsDifferentClass() {
        Edge<String> edge = new Edge<>("A", "B", true);
        String str = "A -> B";

        assertNotEquals(edge, str);
    }

    @Test
    @DisplayName("ToString для направленного ребра")
    void testDirectedEdgeToString() {
        Edge<String> edge = new Edge<>("A", "B", true);

        assertEquals("A -> B", edge.toString());
    }

    @Test
    @DisplayName("ToString для ненаправленного ребра")
    void testUndirectedEdgeToString() {
        Edge<String> edge = new Edge<>("A", "B", false);

        assertEquals("A -- B", edge.toString());
    }

    @Test
    @DisplayName("HashCode для ненаправленных рёбер симметричен")
    void testUndirectedEdgeHashCodeSymmetric() {
        Edge<String> edge1 = new Edge<>("A", "B", false);
        Edge<String> edge2 = new Edge<>("B", "A", false);

        assertEquals(edge1.hashCode(), edge2.hashCode());
    }
}
