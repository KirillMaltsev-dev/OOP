package ru.nsu.maltsev.Task_1_3_1;

import org.junit.jupiter.api.*;
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

    @Test
    @DisplayName("Поиск в простом тексте - основной пример")
    void testBasicSearch() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "бра");

        assertEquals(2, result.size(), "Должно быть найдено 2 вхождения");
        assertEquals(1L, result.get(0), "Первое вхождение на индексе 1");
        assertEquals(8L, result.get(1), "Второе вхождение на индексе 8");
    }

    @Test
    @DisplayName("Поиск пустой подстроки")
    void testEmptyPattern() throws IOException {
        File file = createTestFile("test");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "");

        assertTrue(result.isEmpty(), "Пустая подстрока не должна давать результатов");
    }

    @Test
    @DisplayName("Подстрока не найдена")
    void testPatternNotFound() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "xyz");

        assertTrue(result.isEmpty(), "Подстрока не должна быть найдена");
    }

    @Test
    @DisplayName("Поиск в конце текста")
    void testPatternAtEnd() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "абра");

        assertTrue(result.contains(0L), "Должно содержать вхождение в начале");
    }

    @Test
    @DisplayName("Перекрывающиеся вхождения")
    void testOverlappingPatterns() throws IOException {
        File file = createTestFile("ааааа");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "ааа");

        assertEquals(3, result.size(), "Должно быть 3 перекрывающихся вхождения");
        assertEquals(0L, result.get(0));
        assertEquals(1L, result.get(1));
        assertEquals(2L, result.get(2));
    }

    @Test
    @DisplayName("Поиск одного символа")
    void testSingleCharacter() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "а");

        assertEquals(5, result.size(), "Символ 'а' встречается 5 раз");
    }

    @Test
    @DisplayName("Поиск всей строки")
    void testWholeString() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "абракадабра");

        assertEquals(1, result.size());
        assertEquals(0L, result.get(0));
    }

    @Test
    @DisplayName("Подстрока длиннее текста")
    void testPatternLongerThanText() throws IOException {
        File file = createTestFile("абра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "абракадабра");

        assertTrue(result.isEmpty(), "Подстрока длиннее текста не должна быть найдена");
    }

    @Test
    @DisplayName("Пустой файл")
    void testEmptyFile() throws IOException {
        File file = createTestFile("");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "test");

        assertTrue(result.isEmpty(), "В пустом файле ничего не должно быть найдено");
    }

    @Test
    @DisplayName("UTF-8: кириллица")
    void testUTF8Cyrillic() throws IOException {
        File file = createTestFile("Привет мир привет");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "привет");

        assertEquals(1, result.size(), "Должно найти одно вхождение (с учётом регистра)");
        assertEquals(11L, result.get(0));
    }

    @Test
    @DisplayName("UTF-8: смешанный текст")
    void testUTF8Mixed() throws IOException {
        File file = createTestFile("hello мир world");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "мир");

        assertEquals(1, result.size());
        assertEquals(6L, result.get(0));
    }

    @Test
    @DisplayName("Большой файл (симуляция)")
    void testLargeFile() throws IOException {
        // Создаём файл ~100KB
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            content.append("абракадабра ");
        }
        File file = createTestFile(content.toString());

        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "бра");

        assertTrue(result.size() >= 2000, "Должно найти много вхождений");
    }

    @Test
    @DisplayName("Несуществующий файл выбрасывает исключение")
    void testNonExistentFile() {
        assertThrows(IOException.class, () -> {
            StreamingKMPSearcher.searchInFile("nonexistent_file.txt", "test");
        });
    }

    @Test
    @DisplayName("searchInFileAsInt корректно работает")
    void testSearchInFileAsInt() throws IOException {
        File file = createTestFile("абракадабра");
        List<Integer> result = StreamingKMPSearcher.searchInFileAsInt(file.getAbsolutePath(), "бра");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0));
        assertEquals(8, result.get(1));
    }

    @Test
    @DisplayName("Множественные вхождения подряд")
    void testMultipleConsecutiveMatches() throws IOException {
        File file = createTestFile("бра бра бра");
        List<Long> result = StreamingKMPSearcher.searchInFile(file.getAbsolutePath(), "бра");

        assertEquals(3, result.size());
        assertEquals(0L, result.get(0));
        assertEquals(4L, result.get(1));
        assertEquals(8L, result.get(2));
    }
}
