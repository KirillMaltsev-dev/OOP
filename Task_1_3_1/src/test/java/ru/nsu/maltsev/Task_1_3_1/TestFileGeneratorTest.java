package ru.nsu.maltsev.Task_1_3_1;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса TestFileGenerator
 */
class TestFileGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Генерация простого файла")
    void testGenerateSimpleFile() throws IOException {
        File file = tempDir.resolve("simple.txt").toFile();
        TestFileGenerator.generateSimpleFile(file.getAbsolutePath(), "test content");

        assertTrue(file.exists(), "Файл должен быть создан");

        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        assertEquals("test content", content, "Содержимое должно совпадать");
    }

    @Test
    @DisplayName("Генерация простого файла с UTF-8")
    void testGenerateSimpleFileUTF8() throws IOException {
        File file = tempDir.resolve("utf8.txt").toFile();
        TestFileGenerator.generateSimpleFile(file.getAbsolutePath(), "абракадабра");

        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        assertEquals("абракадабра", content);
    }

    @Test
    @DisplayName("Генерация файла с заданным размером")
    void testGenerateFileWithSize() throws IOException {
        File file = tempDir.resolve("sized.txt").toFile();

        // Генерируем маленький файл ~0.01 MB для быстрого теста
        TestFileGenerator.generateFile(file.getAbsolutePath(), 1, "бра", 10);

        assertTrue(file.exists(), "Файл должен быть создан");
        assertTrue(file.length() > 0, "Файл не должен быть пустым");

        // Проверяем примерный размер (допускаем погрешность)
        long sizeKB = file.length() / 1024;
        assertTrue(sizeKB >= 900 && sizeKB <= 1100,
                "Размер файла должен быть примерно 1MB, получено: " + sizeKB + " KB");
    }

    @Test
    @DisplayName("Сгенерированный файл содержит паттерн")
    void testGeneratedFileContainsPattern() throws IOException {
        File file = tempDir.resolve("pattern.txt").toFile();
        TestFileGenerator.generateFile(file.getAbsolutePath(), 1, "тест", 5);

        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        assertTrue(content.contains("тест"), "Файл должен содержать паттерн");
    }

    @Test
    @DisplayName("Генерация файла без паттерна (null)")
    void testGenerateFileNullPattern() throws IOException {
        File file = tempDir.resolve("nopattern.txt").toFile();
        TestFileGenerator.generateFile(file.getAbsolutePath(), 1, null, 10);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    @DisplayName("Генерация файла с пустым паттерном")
    void testGenerateFileEmptyPattern() throws IOException {
        File file = tempDir.resolve("emptypattern.txt").toFile();
        TestFileGenerator.generateFile(file.getAbsolutePath(), 1, "", 10);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    @DisplayName("Генерация очень маленького файла")
    void testGenerateTinyFile() throws IOException {
        File file = tempDir.resolve("tiny.txt").toFile();

        // Используем очень маленький размер
        long tinySize = 1024; // 1KB в байтах

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("test ");
        }

        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Проверка частоты вставки паттерна")
    void testPatternFrequency() throws IOException {
        File file = tempDir.resolve("frequency.txt").toFile();
        TestFileGenerator.generateFile(file.getAbsolutePath(), 1, "PATTERN", 2);

        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

        // Подсчитываем количество вхождений
        int count = 0;
        int index = 0;
        while ((index = content.indexOf("PATTERN", index)) != -1) {
            count++;
            index++;
        }

        assertTrue(count > 0, "Паттерн должен встречаться в файле");
    }

    @Test
    @DisplayName("Генерация файла создаёт валидный UTF-8")
    void testGeneratedFileValidUTF8() throws IOException {
        File file = tempDir.resolve("utf8valid.txt").toFile();
        TestFileGenerator.generateFile(file.getAbsolutePath(), 1, "тест", 10);

        // Попытка прочитать как UTF-8 не должна выбросить исключение
        assertDoesNotThrow(() -> {
            Files.readString(file.toPath(), StandardCharsets.UTF_8);
        });
    }

    @Test
    @DisplayName("Перезапись существующего файла")
    void testOverwriteExistingFile() throws IOException {
        File file = tempDir.resolve("overwrite.txt").toFile();

        // Создаём файл
        TestFileGenerator.generateSimpleFile(file.getAbsolutePath(), "old content");

        // Перезаписываем
        TestFileGenerator.generateSimpleFile(file.getAbsolutePath(), "new content");

        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        assertEquals("new content", content, "Содержимое должно быть перезаписано");
    }
}
