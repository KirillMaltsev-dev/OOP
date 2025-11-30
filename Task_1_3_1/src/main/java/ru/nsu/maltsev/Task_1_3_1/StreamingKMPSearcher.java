package ru.nsu.maltsev.Task_1_3_1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * КМП-поиск с потоковым чтением файла (для файлов больше RAM)
 */
public class StreamingKMPSearcher {

    private static final int BUFFER_SIZE = 8192; // 8KB буфер

    /**
     * Построение префикс-функции для алгоритма КМП
     */
    private static int[] buildPrefixFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        pi[0] = 0;

        for (int i = 1; i < m; i++) {
            int j = pi[i - 1];

            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }

            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }

    /**
     * Потоковый поиск подстроки в файле
     * Читает файл блоками, не загружая весь в память
     */
    public static List<Long> searchInFile(String fileName, String pattern) throws IOException {
        List<Long> results = new ArrayList<>();

        if (pattern.isEmpty()) {
            return results;
        }

        int[] pi = buildPrefixFunction(pattern);
        int m = pattern.length();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8),
                BUFFER_SIZE)) {

            long globalPosition = 0;
            int j = 0;

            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;

            while ((charsRead = reader.read(buffer, 0, BUFFER_SIZE)) != -1) {

                for (int i = 0; i < charsRead; i++) {
                    char currentChar = buffer[i];

                    while (j > 0 && currentChar != pattern.charAt(j)) {
                        j = pi[j - 1];
                    }

                    if (currentChar == pattern.charAt(j)) {
                        j++;
                    }

                    if (j == m) {
                        long matchIndex = globalPosition + i - m + 1;
                        results.add(matchIndex);

                        j = pi[j - 1];
                    }
                }
                globalPosition += charsRead;
            }
        }

        return results;
    }

    /**
     * Вспомогательный метод для возврата List<Integer> (для совместимости)
     */
    public static List<Integer> searchInFileAsInt(String fileName, String pattern) throws IOException {
        List<Long> longResults = searchInFile(fileName, pattern);
        List<Integer> results = new ArrayList<>();

        for (Long index : longResults) {
            if (index <= Integer.MAX_VALUE) {
                results.add(index.intValue());
            } else {
                System.err.println("Предупреждение: индекс " + index +
                        " слишком большой для int, используйте searchInFile()");
            }
        }

        return results;
    }
}

