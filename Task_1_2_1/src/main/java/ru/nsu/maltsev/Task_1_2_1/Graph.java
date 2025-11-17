package ru.nsu.maltsev.Task_1_2_1;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Интерфейс для представления графа с вершинами типа V.
 * Поддерживает различные способы хранения графа.
 *
 * @param <V> тип вершин графа
 */
public interface Graph<V> {

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex вершина для добавления
     * @return true, если вершина была добавлена, false если уже существовала
     */
    boolean addVertex(V vertex);

    /**
     * Удаляет вершину из графа вместе со всеми инцидентными рёбрами.
     *
     * @param vertex вершина для удаления
     * @return true, если вершина была удалена, false если не существовала
     */
    boolean removeVertex(V vertex);

    /**
     * Добавляет ребро между двумя вершинами.
     * Если граф неориентированный, ребро добавляется в обе стороны.
     *
     * @param from начальная вершина
     * @param to конечная вершина
     * @return true, если ребро было добавлено, false если уже существовало
     * @throws IllegalArgumentException если одна из вершин не существует
     */
    boolean addEdge(V from, V to);

    /**
     * Удаляет ребро между двумя вершинами.
     *
     * @param from начальная вершина
     * @param to конечная вершина
     * @return true, если ребро было удалено, false если не существовало
     */
    boolean removeEdge(V from, V to);

    /**
     * Возвращает список всех соседей указанной вершины.
     *
     * @param vertex вершина, соседей которой нужно получить
     * @return список соседних вершин
     * @throws IllegalArgumentException если вершина не существует
     */
    List<V> getNeighbors(V vertex);

    /**
     * Возвращает множество всех вершин графа.
     *
     * @return множество вершин
     */
    Set<V> getVertices();

    /**
     * Проверяет наличие ребра между двумя вершинами.
     *
     * @param from начальная вершина
     * @param to конечная вершина
     * @return true, если ребро существует
     */
    boolean hasEdge(V from, V to);

    /**
     * Возвращает количество вершин в графе.
     *
     * @return количество вершин
     */
    int getVertexCount();

    /**
     * Возвращает количество рёбер в графе.
     *
     * @return количество рёбер
     */
    int getEdgeCount();

    /**
     * Проверяет, является ли граф ориентированным.
     *
     * @return true, если граф ориентированный
     */
    boolean isDirected();

    /**
     * Читает граф из файла фиксированного формата.
     * Формат файла:
     * Первая строка: DIRECTED или UNDIRECTED
     * Далее: пары вершин (from to) по одной на строку
     *
     * @param filename имя файла
     * @throws IOException если возникла ошибка при чтении файла
     */
    void readFromFile(String filename) throws IOException;

    /**
     * Возвращает строковое представление графа.
     *
     * @return строковое представление
     */
    @Override
    String toString();

    /**
     * Сравнивает два графа на равенство.
     * Графы равны, если имеют одинаковые множества вершин и рёбер.
     *
     * @param obj объект для сравнения
     * @return true, если графы равны
     */
    @Override
    boolean equals(Object obj);

    /**
     * Возвращает хэш-код графа.
     *
     * @return хэш-код
     */
    @Override
    int hashCode();
}
