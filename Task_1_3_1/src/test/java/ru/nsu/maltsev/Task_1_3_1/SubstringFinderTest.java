package ru.nsu.maltsev.Task_1_3_1;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Расширенные тесты для класса SubstringFinder
 */
class SubstringFinderTest {

    @TempDir
    Path tempDir;

    private File createTestFile(String content) throws IOException {
        File file = tempDir.resolve("test.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
        return file;
    }

    @Test
    @DisplayName("Метод find() находит вхождения")
    void testFindMethod() throws IOException {
        File file = createTestFile("абракадабра");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "бра");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0));
        assertEquals(8, result.get(1));
    }

    @Test
    @DisplayName("Метод find() обрабатывает пустую подстроку")
    void testFindEmptyPattern() throws IOException {
        File file = createTestFile("test");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Метод find() обрабатывает несуществующий файл")
    void testFindNonExistentFile() {
        List<Integer> result = SubstringFinder.find("nonexistent.txt", "test");

        assertTrue(result.isEmpty(), "Должен вернуть пустой список при ошибке");
    }

    @Test
    @DisplayName("Метод findLong() работает корректно")
    void testFindLongMethod() throws IOException {
        File file = createTestFile("абракадабра");
        List<Long> result = SubstringFinder.findLong(file.getAbsolutePath(), "бра");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0));
        assertEquals(8L, result.get(1));
    }

    @Test
    @DisplayName("Метод findLong() обрабатывает ошибки")
    void testFindLongError() {
        List<Long> result = SubstringFinder.findLong("nonexistent.txt", "test");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Поиск в файле с переносами строк")
    void testFindWithNewlines() throws IOException {
        File file = createTestFile("абра\nкадабра");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "бра");

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Поиск специальных символов")
    void testFindSpecialCharacters() throws IOException {
        File file = createTestFile("test@test#test");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "@");

        assertEquals(1, result.size());
        assertEquals(4, result.get(0));
    }

    @Test
    @DisplayName("Множественный поиск в одном файле")
    void testMultipleSearches() throws IOException {
        File file = createTestFile("тест1 тест2 тест3");

        List<Integer> result1 = SubstringFinder.find(file.getAbsolutePath(), "тест");
        assertEquals(3, result1.size());

        List<Integer> result2 = SubstringFinder.find(file.getAbsolutePath(), "1");
        assertEquals(1, result2.size());
    }

    @Test
    @DisplayName("Поиск в большом файле")
    void testLargeFileSearch() throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            content.append("тест ");
        }
        File file = createTestFile(content.toString());

        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "тест");
        assertEquals(10000, result.size());
    }

    @Test
    @DisplayName("Поиск с различными кодировками символов")
    void testUTF8Characters() throws IOException {
        File file = createTestFile("🎉test🎉test🎉");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "test");

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Поиск подстроки в начале файла")
    void testPatternAtFileStart() throws IOException {
        File file = createTestFile("начало текста");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "начало");

        assertEquals(1, result.size());
        assertEquals(0, result.get(0));
    }

    @Test
    @DisplayName("Поиск подстроки в конце файла")
    void testPatternAtFileEnd() throws IOException {
        File file = createTestFile("текст конец");
        List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), "конец");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Сравнение find() и findLong()")
    void testFindVsFindLong() throws IOException {
        File file = createTestFile("абракадабра");

        List<Integer> intResult = SubstringFinder.find(file.getAbsolutePath(), "а");
        List<Long> longResult = SubstringFinder.findLong(file.getAbsolutePath(), "а");

        assertEquals(intResult.size(), longResult.size());
        for (int i = 0; i < intResult.size(); i++) {
            assertEquals(intResult.get(i).longValue(), longResult.get(i));
        }
    }

    @Test
    @DisplayName("Обработка null в качестве pattern")
    void testNullPattern() throws IOException {
        File file = createTestFile("test");

        // Должен вернуть пустой список или выбросить исключение
        assertDoesNotThrow(() -> {
            List<Integer> result = SubstringFinder.find(file.getAbsolutePath(), null);
            // Может вернуть пустой список
        });
    }
}
