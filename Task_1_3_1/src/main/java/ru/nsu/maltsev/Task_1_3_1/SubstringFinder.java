package ru.nsu.maltsev.Task_1_3_1;

import java.util.List;

/**
 * Главный класс для поиска подстроки в файле.
 * Поддерживает файлы любого размера (больше RAM).
 */
public class SubstringFinder {

    /**
     * Главный метод для поиска всех вхождений подстроки в файле.
     * Использует потоковое чтение для работы с большими файлами.
     *
     * @param fileName имя файла для поиска
     * @param pattern искомая подстрока
     * @return список индексов начала каждого вхождения
     */
    public static List<Integer> find(String fileName, String pattern) {
        try {
            return StreamingKMPSearcher.searchInFileAsInt(fileName, pattern);
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Версия для очень больших файлов (индексы могут превышать int).
     *
     * @param fileName имя файла для поиска
     * @param pattern искомая подстрока
     * @return список индексов начала каждого вхождения (Long)
     */
    public static List<Long> findLong(String fileName, String pattern) {
        try {
            return StreamingKMPSearcher.searchInFile(fileName, pattern);
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
            return List.of();
        }
    }
}
