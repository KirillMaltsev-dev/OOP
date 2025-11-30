package ru.nsu.maltsev.Task_1_3_1;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Главный класс для поиска подстроки в файле
 * Поддерживает файлы любого размера (больше RAM)
 */
public class SubstringFinder {

    /**
     * Главный метод для поиска всех вхождений подстроки в файле
     * Использует потоковое чтение для работы с большими файлами
     * fileName - имя файла для поиска
     * pattern - искомая подстрока
     * @return список индексов начала каждого вхождения
     */
    public static List<Integer> find(String fileName, String pattern) {
        try {
            return StreamingKMPSearcher.searchInFileAsInt(fileName, pattern);

        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Версия для очень больших файлов
     */
    public static List<Long> findLong(String fileName, String pattern) {
        try {
            return StreamingKMPSearcher.searchInFile(fileName, pattern);
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Точка входа программы - интерактивный режим
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Поиск подстроки в файле (КМП)         ║");
        System.out.println("║  Поддержка файлов больше RAM           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        // Выбор режима
        System.out.println("Выберите режим:");
        System.out.println("1 - Поиск в файле");
        System.out.println("2 - Генерация тестовых файлов");
        System.out.println("3 - Запустить тесты");
        System.out.print("Ваш выбор: ");

        String choice = scanner.nextLine().trim();
        System.out.println();

        if (choice.equals("1")) {
            // Режим поиска
            runSearchMode(scanner);
        } else if (choice.equals("2")) {
            // Режим генерации файлов
            runGenerationMode(scanner);
        } else if (choice.equals("3")) {
            // Режим тестирования
            runTestMode();
        } else {
            System.out.println("Неверный выбор. Завершение программы.");
        }

        scanner.close();
    }

    /**
     * Режим поиска в файле
     */
    private static void runSearchMode(Scanner scanner) {
        System.out.print("Введите имя файла (например, input.txt): ");
        String fileName = scanner.nextLine().trim();

        System.out.print("Введите искомую подстроку: ");
        String pattern = scanner.nextLine();

        System.out.println();
        System.out.println("Поиск подстроки \"" + pattern + "\" в файле \"" + fileName + "\"...");

        long startTime = System.currentTimeMillis();
        List<Integer> result = find(fileName, pattern);
        long endTime = System.currentTimeMillis();

        System.out.println();
        if (result.isEmpty()) {
            System.out.println("Подстрока не найдена.");
        } else {
            System.out.println("Найдено вхождений: " + result.size());

            // Показываем первые 10 индексов
            if (result.size() <= 10) {
                System.out.println("Индексы: " + result);
            } else {
                System.out.println("Первые 10 индексов: " + result.subList(0, 10));
                System.out.println("... и ещё " + (result.size() - 10) + " вхождений");
            }
        }

        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
    }

    /**
     * Режим генерации тестовых файлов
     */
    private static void runGenerationMode(Scanner scanner) {
        try {
            new File("test-data/generated").mkdirs();

            System.out.print("Введите имя файла (например, test.txt): ");
            String fileName = "test-data/generated/" + scanner.nextLine().trim();

            System.out.print("Введите размер в MB: ");
            long sizeMB = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Введите подстроку для вставки: ");
            String pattern = scanner.nextLine();

            System.out.print("Частота вставки (каждые N слов): ");
            int frequency = Integer.parseInt(scanner.nextLine().trim());

            System.out.println();
            TestFileGenerator.generateFile(fileName, sizeMB, pattern, frequency);

        } catch (Exception e) {
            System.err.println("Ошибка при генерации: " + e.getMessage());
        }
    }

    /**
     * Режим автоматического тестирования
     */
    private static void runTestMode() {
        System.out.println("=== Запуск тестов ===\n");

        // Тест 1: Основной пример
        System.out.println("Тест 1: input.txt");
        List<Integer> result1 = find("input.txt", "бра");
        System.out.println("Результат: " + result1);
        System.out.println("Ожидалось: [1, 8]");
        System.out.println(result1.equals(List.of(1, 8)) ? "PASSED" : "FAILED");
        System.out.println();

        // Тест 2: Маленький тестовый файл
        try {
            TestFileGenerator.generateSimpleFile("test-data/test_small.txt", "абракадабра");
            System.out.println("Тест 2: Маленький файл");
            List<Integer> result2 = find("test-data/test_small.txt", "бра");
            System.out.println("Результат: " + result2);
            System.out.println("Ожидалось: [1, 8]");
            System.out.println(result2.equals(List.of(1, 8)) ? "PASSED" : "FAILED");
            System.out.println();
        } catch (Exception e) {
            System.err.println("Ошибка в тесте 2: " + e.getMessage());
        }

        System.out.println("Тесты завершены!");
    }
}
