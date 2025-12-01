package ru.nsu.maltsev.Task_1_3_1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Генератор больших файлов для тестирования
 */
public class TestFileGenerator {

    private static final String[] RUSSIAN_WORDS = {
            "абра", "кадабра", "алабама", "бразилия", "граната",
            "драма", "трава", "слово", "текст", "строка", "ООП"
    };

    /**
     * Генерирует файл заданного размера со случайным текстом.
     *
     * @param fileName имя выходного файла
     * @param sizeInMB размер файла в мегабайтах
     * @param pattern строка, которая будет встречаться с заданной частотой
     * @param patternFrequency как часто вставлять pattern (каждые N слов)
     * @throws IOException если возникла ошибка при записи файла
     */
    public static void generateFile(String fileName, long sizeInMB,
                                    String pattern, int patternFrequency) throws IOException {
        long targetSize = sizeInMB * 1024 * 1024; // в байтах
        long bytesWritten = 0;
        int wordCounter = 0;

        Random random = new Random(42);

        System.out.println("Генерация файла: " + fileName);
        System.out.println("Целевой размер: " + sizeInMB + " MB");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8),
                8192)) {

            while (bytesWritten < targetSize) {
                String word;

                if (wordCounter % patternFrequency == 0 && pattern != null && !pattern.isEmpty()) {
                    word = pattern;
                } else {
                    word = RUSSIAN_WORDS[random.nextInt(RUSSIAN_WORDS.length)];
                }

                writer.write(word);
                writer.write(" ");

                bytesWritten += word.getBytes(StandardCharsets.UTF_8).length + 1;
                wordCounter++;

                if (wordCounter % 20 == 0) {
                    writer.write("\n");
                    bytesWritten += 1;
                }

                // Прогресс каждые 10 MB
                if (bytesWritten % (10 * 1024 * 1024) == 0) {
                    System.out.printf("Записано: %d MB\n", bytesWritten / (1024 * 1024));
                }
            }
        }

        System.out.println("Файл сгенерирован: " + fileName);
        System.out.println("Финальный размер: " + (bytesWritten / (1024.0 * 1024.0)) + " MB");
    }

    /**
     * Генерирует простой тестовый файл с заданным содержимым.
     *
     * @param fileName имя выходного файла
     * @param content содержимое файла
     * @throws IOException если возникла ошибка при записи файла
     */
    public static void generateSimpleFile(String fileName, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
        System.out.println("Создан файл: " + fileName);
    }

    /**
     * Примеры использования и тесты
     */
    public static void main(String[] args) {
        try {
            // Создаём директорию для тестовых данных
            new File("test-data/generated").mkdirs();

            System.out.println("=== Генерация тестовых файлов ===\n");

            // 1. Маленький файл
            System.out.println("1. Маленький тестовый файл");
            generateSimpleFile("test-data/small_test.txt", "абракадабра бразилия абрам");
            System.out.println();

            // 2. Средний файл (1 MB)
            System.out.println("2. Средний файл (1 MB)");
            generateFile("test-data/generated/medium_test.txt", 1, "бра", 10);
            System.out.println();

            // 3. Большой файл (100 MB)
            System.out.println("3. Большой файл (100 MB)");
            generateFile("test-data/generated/large_test.txt", 100, "бра", 50);
            System.out.println();

            // 4. Очень большой файл (1 GB)
            System.out.println("4. Очень большой файл (1 GB)");
            generateFile("test-data/generated/huge_test.txt", 1024, "бра", 100);

            System.out.println("Генерация завершена!");
            System.out.println("Большие файлы находятся в test-data/generated/ (не добавляются в git)");

        } catch (IOException e) {
            System.err.println("Ошибка при генерации файлов: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
