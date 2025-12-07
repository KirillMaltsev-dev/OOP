package ru.nsu.maltsev.Task_1_3_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * КМП-поиск с потоковым чтением файла (для файлов больше RAM).
 */
public class StreamingKMPSearcher {

    private static final int BUFFER_SIZE = 8192; // 8KB буфер

    /**
     * Потоковый поиск подстроки в файле с поддержкой полных Unicode символов (включая суррогатные пары).
     * Читает данные из Reader блоками.
     *
     * @param reader  читатель данных (файл, строка и т.д.)
     * @param pattern искомая подстрока
     * @return список индексов начала каждого вхождения (считая по символам Unicode/code points)
     * @throws IOException если возникла ошибка при чтении
     */
    public static List<Long> searchInStream(BufferedReader reader, String pattern) throws IOException {
        List<Long> results = new ArrayList<>();

        if (pattern == null || pattern.isEmpty()) {
            return results;
        }

        // Преобразуем паттерн в кодовые точки (code points)
        int[] patternCodePoints = pattern.codePoints().toArray();
        int[] pi = buildPrefixFunction(patternCodePoints);
        int m = patternCodePoints.length;

        long globalCodePointIndex = 0; // Глобальная позиция в Code Points
        int j = 0; // Количество совпавших символов образца

        int charVal;
        while ((charVal = reader.read()) != -1) {
            int currentCodePoint = charVal;

            // Если это высокий суррогат, нужно прочитать следующий char (низкий суррогат)
            // чтобы получить полный code point
            if (Character.isHighSurrogate((char) charVal)) {
                int nextCharVal = reader.read();
                if (nextCharVal != -1) {
                    currentCodePoint = Character.toCodePoint((char) charVal, (char) nextCharVal);
                }
            }

            // КМП логика для code points
            while (j > 0 && currentCodePoint != patternCodePoints[j]) {
                j = pi[j - 1];
            }

            if (currentCodePoint == patternCodePoints[j]) {
                j++;
            }

            if (j == m) {
                // Индекс начала вхождения (в code points)
                results.add(globalCodePointIndex - m + 1);
                j = pi[j - 1];
            }

            // Увеличиваем индекс на 1 для каждого полного Unicode символа (code point)
            globalCodePointIndex++;
        }

        return results;
    }

    /**
     * Построение префикс-функции для алгоритма КМП.
     *
     * @param patternCodePoints массив кодовых точек образца
     * @return массив значений префикс-функции
     */
    private static int[] buildPrefixFunction(int[] patternCodePoints) {
        int m = patternCodePoints.length;
        int[] pi = new int[m];
        pi[0] = 0;

        for (int i = 1; i < m; i++) {
            int j = pi[i - 1];

            while (j > 0 && patternCodePoints[i] != patternCodePoints[j]) {
                j = pi[j - 1];
            }

            if (patternCodePoints[i] == patternCodePoints[j]) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }
}
