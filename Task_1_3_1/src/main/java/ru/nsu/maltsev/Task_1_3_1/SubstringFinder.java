package ru.nsu.maltsev.Task_1_3_1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс для поиска подстроки в файле.
 * Поддерживает файлы любого размера (больше RAM).
 */
public class SubstringFinder {

    /**
     * Главный метод для поиска всех вхождений подстроки в файле.
     *
     * @param fileName имя файла для поиска
     * @param pattern  искомая подстрока
     * @return список индексов начала каждого вхождения
     */
    public static List<Integer> find(String fileName, String pattern) {
        List<Long> longResults = findLong(fileName, pattern);
        List<Integer> results = new ArrayList<>();

        for (Long index : longResults) {
            if (index <= Integer.MAX_VALUE) {
                results.add(index.intValue());
            } else {
                System.err.println("Предупреждение: индекс " + index + " слишком большой для int");
            }
        }
        return results;
    }

    /**
     * Версия для очень больших файлов (индексы могут превышать int).
     *
     * @param fileName имя файла для поиска
     * @param pattern  искомая подстрока
     * @return список индексов начала каждого вхождения (Long)
     */
    public static List<Long> findLong(String fileName, String pattern) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8),
                8192)) {

            return StreamingKMPSearcher.searchInStream(reader, pattern);

        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
            return List.of();
        }
    }
}
