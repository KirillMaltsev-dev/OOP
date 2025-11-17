package ru.nsu.maltsev.Task_1_2_1;

import java.util.Objects;

/**
 * Класс, представляющий ребро графа.
 * Используется для унификации работы с рёбрами в различных реализациях.
 *
 * @param <V> тип вершин
 */
public class Edge<V> {
    private final V from;
    private final V to;
    private final boolean directed;

    /**
     * Создаёт новое ребро.
     *
     * @param from начальная вершина
     * @param to конечная вершина
     * @param directed является ли ребро направленным
     */
    public Edge(V from, V to, boolean directed) {
        this.from = from;
        this.to = to;
        this.directed = directed;
    }

    /**
     * Возвращает начальную вершину ребра.
     *
     * @return начальная вершина
     */
    public V getFrom() {
        return from;
    }

    /**
     * Возвращает конечную вершину ребра.
     *
     * @return конечная вершина
     */
    public V getTo() {
        return to;
    }

    /**
     * Проверяет, является ли ребро направленным.
     *
     * @return true, если ребро направленное
     */
    public boolean isDirected() {
        return directed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Edge<?> edge = (Edge<?>) obj;

        if (directed) {
            // Для ориентированного ребра важен порядок
            return Objects.equals(from, edge.from)
                    && Objects.equals(to, edge.to);
        } else {
            // Для неориентированного ребра порядок не важен
            return (Objects.equals(from, edge.from) && Objects.equals(to, edge.to))
                    || (Objects.equals(from, edge.to) && Objects.equals(to, edge.from));
        }
    }

    @Override
    public int hashCode() {
        if (directed) {
            return Objects.hash(from, to);
        } else {
            // Для неориентированного ребра хэш должен быть одинаковым
            // независимо от порядка вершин
            return Objects.hash(from, to) + Objects.hash(to, from);
        }
    }

    @Override
    public String toString() {
        return directed
                ? String.format("%s -> %s", from, to)
                : String.format("%s -- %s", from, to);
    }
}
