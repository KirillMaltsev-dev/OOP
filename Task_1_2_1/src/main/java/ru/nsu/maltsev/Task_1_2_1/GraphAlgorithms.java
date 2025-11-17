package ru.nsu.maltsev.Task_1_2_1;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Класс с алгоритмами для работы с графами.
 */
public class GraphAlgorithms {

    /**
     * Выполняет топологическую сортировку вершин графа методом DFS.
     * Топологическая сортировка возможна только для направленных ациклических графов (DAG).
     *
     * Алгоритм:
     * 1. Выполняем DFS для каждой непосещённой вершины
     * 2. После обработки всех соседей вершины добавляем её в результат
     * 3. Разворачиваем результат для получения правильного порядка
     *
     * @param graph граф для сортировки
     * @param <V> тип вершин графа
     * @return список вершин в топологическом порядке
     * @throws IllegalArgumentException если граф не является направленным
     * @throws IllegalStateException если граф содержит цикл
     */
    public static <V> List<V> topologicalSort(Graph<V> graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Topological sort is only possible for directed graphs");
        }

        Set<V> visited = new HashSet<>();
        Set<V> recursionStack = new HashSet<>();
        List<V> result = new ArrayList<>();

        // Запускаем DFS для каждой непосещённой вершины
        for (V vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                dfsTopologicalSort(graph, vertex, visited, recursionStack, result);
            }
        }

        // Разворачиваем результат
        Collections.reverse(result);
        return result;
    }

    /**
     * Рекурсивный обход в глубину для топологической сортировки.
     *
     * @param graph граф
     * @param vertex текущая вершина
     * @param visited множество посещённых вершин
     * @param recursionStack стек рекурсии для обнаружения циклов
     * @param result список результата
     * @param <V> тип вершин
     * @throws IllegalStateException если обнаружен цикл
     */
    private static <V> void dfsTopologicalSort(
            Graph<V> graph,
            V vertex,
            Set<V> visited,
            Set<V> recursionStack,
            List<V> result) {

        // Если вершина в стеке рекурсии — найден цикл
        if (recursionStack.contains(vertex)) {
            throw new IllegalStateException(
                    "Graph contains a cycle. Topological sort is not possible. Cycle detected at vertex: "
                            + vertex);
        }

        // Если вершина уже посещена — пропускаем
        if (visited.contains(vertex)) {
            return;
        }

        // Помечаем вершину как посещённую и добавляем в стек рекурсии
        visited.add(vertex);
        recursionStack.add(vertex);

        // Рекурсивно обрабатываем всех соседей
        for (V neighbor : graph.getNeighbors(vertex)) {
            dfsTopologicalSort(graph, neighbor, visited, recursionStack, result);
        }

        // Убираем из стека рекурсии после обработки всех соседей
        recursionStack.remove(vertex);

        // Добавляем вершину в результат после обработки всех её соседей
        result.add(vertex);
    }
}
