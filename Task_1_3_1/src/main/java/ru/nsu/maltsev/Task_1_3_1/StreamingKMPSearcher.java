package ru.nsu.maltsev.Task_1_3_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * КМП-поиск с потоковым чтением файла.
 * Считает индексы по визуальным символам (Grapheme Clusters).
 */
public class StreamingKMPSearcher {

    private static final int BUFFER_SIZE = 8192;

    public static List<Long> searchInStream(BufferedReader reader, String pattern) throws IOException {
        List<Long> results = new ArrayList<>();
        if (pattern == null || pattern.isEmpty()) {
            return results;
        }

        int[] patternCodePoints = pattern.codePoints().toArray();
        int[] pi = buildPrefixFunction(patternCodePoints);
        int m = patternCodePoints.length;

        //Считаем длину паттерна в Графемах (визуальных символах)
        int patternGraphemeLength = countGraphemes(patternCodePoints);

        long graphemeIndex = 0;
        int prevCodePoint = -1;
        boolean isFirst = true;

        int j = 0;
        int charVal;

        while ((charVal = reader.read()) != -1) {
            int currentCodePoint = charVal;

            if (Character.isHighSurrogate((char) charVal)) {
                int nextCharVal = reader.read();
                if (nextCharVal != -1) {
                    currentCodePoint = Character.toCodePoint((char) charVal, (char) nextCharVal);
                }
            }

            //Логика подсчета визуальных индексов
            if (!isFirst) {
                if (isGraphemeBoundary(prevCodePoint, currentCodePoint)) {
                    graphemeIndex++;
                }
            }
            isFirst = false;
            prevCodePoint = currentCodePoint;

            while (j > 0 && currentCodePoint != patternCodePoints[j]) {
                j = pi[j - 1];
            }

            if (currentCodePoint == patternCodePoints[j]) {
                j++;
                if (j == m) {
                    results.add(graphemeIndex - patternGraphemeLength + 1);
                    j = pi[j - 1];
                }
            }
        }
        return results;
    }

    private static boolean isGraphemeBoundary(int prev, int curr) {
        if (prev == 0x200D) return false;
        if ((curr >= 0xFE00 && curr <= 0xFE0F) || (curr >= 0xE0100 && curr <= 0xE01EF)) {
            return false;
        }
        if (curr >= 0x1F3FB && curr <= 0x1F3FF) return false;

        int type = Character.getType(curr);
        return type != Character.NON_SPACING_MARK &&
                type != Character.COMBINING_SPACING_MARK &&
                type != Character.ENCLOSING_MARK;
    }

    private static int countGraphemes(int[] codePoints) {
        if (codePoints.length == 0) return 0;
        int count = 1;
        for (int i = 1; i < codePoints.length; i++) {
            if (isGraphemeBoundary(codePoints[i - 1], codePoints[i])) {
                count++;
            }
        }
        return count;
    }

    private static int[] buildPrefixFunction(int[] patternCodePoints) {
        int m = patternCodePoints.length;
        int[] pi = new int[m];
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
