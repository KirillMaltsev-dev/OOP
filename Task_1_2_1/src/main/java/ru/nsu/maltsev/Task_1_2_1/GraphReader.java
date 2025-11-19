package ru.nsu.maltsev.Task_1_2_1;

import java.io.BufferedReader;
import java.io.IOException;

public class GraphReader {
    private final BufferedReader reader;

    public GraphReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Читает граф из файла фиксированного формата.
     * Формат файла:
     * Первая строка: DIRECTED или UNDIRECTED
     * Далее: пары вершин (from to) по одной на строку
     *
     * @param filename имя файла
     * @throws IOException если возникла ошибка при чтении файла
     */
    public <T extends Graph<String>> void readTo(T graph) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Empty file");
        }
        // формат первой строки: DIRECTED или UNDIRECTED
        // уже задан при создании объекта
        // далее ожидаем вершины и рёбра
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 1) {
                graph.addVertex(parts[0]);
            } else if (parts.length == 2) {
                graph.addVertex(parts[0]);
                graph.addVertex(parts[1]);
                graph.addEdge(parts[0], parts[1]);
            }
        }
    }
}
