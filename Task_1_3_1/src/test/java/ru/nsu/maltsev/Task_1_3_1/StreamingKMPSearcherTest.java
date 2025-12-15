package ru.nsu.maltsev.Task_1_3_1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса StreamingKMPSearcher
 */
class StreamingKMPSearcherTest {

    @TempDir
    Path tempDir; // JUnit создаст временную директорию

    /**
     * Вспомогательный метод для создания тестового файла
     */
    private File createTestFile(String content) throws IOException {
        File file = tempDir.resolve("test.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
        return file;
    }

    /**
     * Вспомогательный метод для выполнения поиска через Reader
     */
    private List<Long> performSearch(File file, String pattern) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return StreamingKMPSearcher.searchInStream(reader, pattern);
        }
    }

    @Test
    @DisplayName("Поиск в простом тексте - основной пример")
    void testBasicSearch() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = performSearch(file, "бра");

        assertEquals(2, result.size(), "Должно быть найдено 2 вхождения");
        assertEquals(1L, result.get(0), "Первое вхождение на индексе 1");
        assertEquals(8L, result.get(1), "Второе вхождение на индексе 8");
    }

    @Test
    @DisplayName("Поиск пустой подстроки")
    void testEmptyPattern() throws IOException {
        File file = createTestFile("test");
        List<Long> result = performSearch(file, "");

        assertTrue(result.isEmpty(), "Пустая подстрока не должна давать результатов");
    }

    @Test
    @DisplayName("Подстрока не найдена")
    void testPatternNotFound() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = performSearch(file, "xyz");

        assertTrue(result.isEmpty(), "Подстрока не должна быть найдена");
    }

    @Test
    @DisplayName("Поиск в начале текста")
    void testPatternAtStart() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = performSearch(file, "абрака");

        assertEquals(1, result.size());
        assertEquals(0L, result.get(0), "Вхождение должно быть на индексе 0");
    }

    @Test
    @DisplayName("Перекрывающиеся вхождения")
    void testOverlappingPatterns() throws IOException {
        File file = createTestFile("ааааа");
        List<Long> result = performSearch(file, "ааа");

        assertEquals(3, result.size(), "Должно быть 3 перекрывающихся вхождения");
        assertEquals(0L, result.get(0));
        assertEquals(1L, result.get(1));
        assertEquals(2L, result.get(2));
    }

    @Test
    @DisplayName("UTF-8: кириллица")
    void testUTF8Cyrillic() throws IOException {
        File file = createTestFile("Привет мир привет");
        List<Long> result = performSearch(file, "привет");

        assertEquals(1, result.size(), "Должно найти одно вхождение (с учётом регистра)");
        assertEquals(11L, result.get(0)); // Индекс в code points
    }

    @Test
    @DisplayName("UTF-8: эмодзи (проверка корректной индексации code points)")
    void testEmojiSearch() throws IOException {
        // Используем Unicode Escapes, чтобы избежать проблем с кодировкой самого .java файла
        // U+1F327 (Cloud with Rain) = \uD83C\uDF27
        String cloud = "\uD83C\uDF27";

        File file = createTestFile(cloud + cloud);
        List<Long> result = performSearch(file, cloud);

        assertEquals(2, result.size());
        assertEquals(0L, result.get(0));
        assertEquals(1L, result.get(1));
    }

    @Test
    @DisplayName("Поиск с Reader напрямую (без файла)")
    void testSearchWithReader() throws IOException {
        String content = "Hello World";
        BufferedReader reader = new BufferedReader(new StringReader(content));

        List<Long> result = StreamingKMPSearcher.searchInStream(reader, "World");

        assertEquals(1, result.size());
        assertEquals(6L, result.get(0));
    }
}
