package ru.nsu.maltsev.Task_1_2_1;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТ 1: Создание графа вручную ===");
        testManualGraphCreation();

        System.out.println("\n=== ТЕСТ 2: Чтение графа из файла ===");
        testReadFromFile();

        System.out.println("\n=== ТЕСТ 3: Сравнение реализаций ===");
        testGraphEquality();

        System.out.println("\n=== ТЕСТ 4: Топологическая сортировка ===");
        testTopologicalSort();

        System.out.println("\n=== ТЕСТ 5: Обнаружение цикла ===");
        testCycleDetection();
    }

    private static void testManualGraphCreation() {
        Graph<String> graph = new AdjacencyListGraph<>(true);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");

        System.out.println(graph);
        System.out.println("Соседи A: " + graph.getNeighbors("A"));
        System.out.println("Количество вершин: " + graph.getVertexCount());
        System.out.println("Количество рёбер: " + graph.getEdgeCount());
    }

    private static void testReadFromFile() {
        Graph<String> graph = new AdjacencyListGraph<>(true);
        try {
            graph.readFromFile("graph.txt");
            System.out.println("Граф успешно загружен из файла:");
            System.out.println(graph);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            System.out.println("Создайте файл graph.txt в корне проекта");
        }
    }

    private static void testGraphEquality() {
        // Создаём одинаковые графы разными способами
        Graph<String> list = new AdjacencyListGraph<>(true);
        Graph<String> matrix = new AdjacencyMatrixGraph<>(true);

        // Добавляем одинаковые вершины и рёбра
        for (Graph<String> g : new Graph[]{list, matrix}) {
            g.addVertex("A");
            g.addVertex("B");
            g.addVertex("C");
            g.addEdge("A", "B");
            g.addEdge("B", "C");
        }

        System.out.println("Список смежности:");
        System.out.println(list);
        System.out.println("Матрица смежности:");
        System.out.println(matrix);

        // Проверка, что они хранят одинаковые данные
        System.out.println("Одинаковое количество вершин: " +
                (list.getVertexCount() == matrix.getVertexCount()));
        System.out.println("Одинаковое количество рёбер: " +
                (list.getEdgeCount() == matrix.getEdgeCount()));
        System.out.println("Одинаковые рёбра A->B: " +
                (list.hasEdge("A", "B") == matrix.hasEdge("A", "B")));
    }

    private static void testTopologicalSort() {
        Graph<String> graph = new AdjacencyListGraph<>(true);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");

        List<String> sorted = GraphAlgorithms.topologicalSort(graph);
        System.out.println("Топологический порядок: " + sorted);

        // Проверка корректности
        System.out.println("Проверка: A перед B? " +
                (sorted.indexOf("A") < sorted.indexOf("B")));
        System.out.println("Проверка: B перед C? " +
                (sorted.indexOf("B") < sorted.indexOf("C")));
        System.out.println("Проверка: C перед D? " +
                (sorted.indexOf("C") < sorted.indexOf("D")));
    }

    private static void testCycleDetection() {
        Graph<String> graph = new AdjacencyListGraph<>(true);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A"); // Создаём цикл!

        try {
            List<String> sorted = GraphAlgorithms.topologicalSort(graph);
            System.out.println("ОШИБКА: Цикл не обнаружен!");
        } catch (IllegalStateException e) {
            System.out.println("✓ Цикл успешно обнаружен: " + e.getMessage());
        }
    }
}
