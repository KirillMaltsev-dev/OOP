package ru.nsu.maltsev.task_2_1_1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Main: Главный класс для тестирования и сравнения методов
 * Вывод результатов производится в файлы performance_results.csv и report.txt
 */
public class Main {

    private static final String CSV_FILE = "performance_results.csv";
    private static final String REPORT_FILE = "analysis_report.txt";

    /**
     * Метод для измерения времени выполнения
     */
    private static long measureTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return end - start;
    }

    /**
     * Генерация массива больших простых чисел для тестирования
     */
    private static int[] generateLargePrimes(int count) {
        int[] primes = new int[count];
        int num = 10000000;
        int found = 0;

        while (found < count) {
            if (PrimeChecker.isPrime(num)) {
                primes[found++] = num;
            }
            num++;
        }

        return primes;
    }

    /**
     * Основной метод
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        System.out.println("Запуск Task_2_1_1...");
        System.out.println("Результаты будут сохранены в файлы:");
        System.out.println("   1. " + CSV_FILE + " (данные для графика)");
        System.out.println("   2. " + REPORT_FILE + " (подробный анализ)");

        try (PrintWriter csvWriter = new PrintWriter(CSV_FILE, StandardCharsets.UTF_8.name());
             PrintWriter reportWriter = new PrintWriter(REPORT_FILE, StandardCharsets.UTF_8.name())) {

            csvWriter.println("Method,Threads,Time_ms,Speedup");

            runCorrectnessTests(reportWriter);

            System.out.println("\nГенерация тестовых данных (1000 больших простых чисел)...");
            int[] largeTestSet = generateLargePrimes(1000);

            int maxThreads = Runtime.getRuntime().availableProcessors();
            reportWriter.printf("\n=== Конфигурация системы ===\n");
            reportWriter.printf("Доступно ядер: %d\n", maxThreads);
            reportWriter.printf("Размер тестового набора: %d чисел\n", largeTestSet.length);

            // Прогрев
            System.out.println("Прогрев JVM...");
            warmUp(largeTestSet);

            // 3. Измерение производительности
            System.out.println("Измерение производительности...");

            // --- Sequential ---
            System.out.print("   • Sequential... ");
            double seqTimeMs = testSequential(largeTestSet, csvWriter, reportWriter);
            System.out.println("Готово (" + String.format("%.2f", seqTimeMs) + " мс)");

            // --- Parallel Thread ---
            System.out.print("   • Parallel Thread (1-" + maxThreads + " потоков)... ");
            double[] parallelTimes = testParallelThread(largeTestSet, maxThreads, seqTimeMs, csvWriter, reportWriter);
            System.out.println("Готово");

            // --- Parallel Stream ---
            System.out.print("   • Parallel Stream... ");
            double streamTimeMs = testParallelStream(largeTestSet, maxThreads, seqTimeMs, csvWriter, reportWriter);
            System.out.println("Готово (" + String.format("%.2f", streamTimeMs) + " мс)");

            // 4. Анализ
            performAnalysis(seqTimeMs, parallelTimes, streamTimeMs, maxThreads, reportWriter);

            System.out.println("\nГотово! Файлы успешно созданы.");

        } catch (IOException e) {
            System.err.println("Ошибка при записи файлов: " + e.getMessage());
        }
    }

    private static void warmUp(int[] data) {
        PrimeChecker seq = new SequentialPrimeChecker();
        PrimeChecker par = new ParallelThreadPrimeChecker(4);
        PrimeChecker str = new ParallelStreamPrimeChecker();

        for (int i = 0; i < 5; i++) {
            seq.hasNonPrime(data);
            par.hasNonPrime(data);
            str.hasNonPrime(data);
        }
    }

    private static double testSequential(int[] data, PrintWriter csv, PrintWriter report) {
        PrimeChecker checker = new SequentialPrimeChecker();
        long[] times = new long[10];
        for (int i = 0; i < 10; i++) times[i] = measureTime(() -> checker.hasNonPrime(data));

        double avgMs = Arrays.stream(times).sum() / times.length / 1_000_000.0;

        // Запись в CSV
        csv.printf(Locale.US, "Sequential,1,%.2f,1.00%n", avgMs);

        // Запись в отчет
        report.printf("\n=== 1. Sequential ===\n");
        report.printf("Среднее время: %.2f мс\n", avgMs);

        return avgMs;
    }

    private static double[] testParallelThread(int[] data, int maxThreads, double seqTimeMs, PrintWriter csv, PrintWriter report) {
        double[] results = new double[maxThreads];
        report.printf("\n=== 2. Parallel Thread ===\n");

        for (int t = 1; t <= maxThreads; t++) {
            PrimeChecker checker = new ParallelThreadPrimeChecker(t);
            long[] times = new long[10];
            for (int i = 0; i < 10; i++) times[i] = measureTime(() -> checker.hasNonPrime(data));

            double avgMs = Arrays.stream(times).sum() / times.length / 1_000_000.0;
            results[t-1] = avgMs;
            double speedup = seqTimeMs / avgMs;

            csv.printf(Locale.US, "Parallel_Thread,%d,%.2f,%.2f%n", t, avgMs, speedup);
            report.printf("Потоков: %2d | Время: %6.2f мс | Ускорение: %5.2fx\n", t, avgMs, speedup);
        }
        return results;
    }

    private static double testParallelStream(int[] data, int maxThreads, double seqTimeMs, PrintWriter csv, PrintWriter report) {
        PrimeChecker checker = new ParallelStreamPrimeChecker();
        long[] times = new long[10];
        for (int i = 0; i < 10; i++) times[i] = measureTime(() -> checker.hasNonPrime(data));

        double avgMs = Arrays.stream(times).sum() / times.length / 1_000_000.0;
        double speedup = seqTimeMs / avgMs;

        csv.printf(Locale.US, "ParallelStream,%d,%.2f,%.2f%n", maxThreads, avgMs, speedup);

        report.printf("\n=== 3. ParallelStream ===\n");
        report.printf("Время: %.2f мс | Ускорение: %.2fx\n", avgMs, speedup);

        return avgMs;
    }

    private static void performAnalysis(double seqTime, double[] parTimes, double streamTime, int threads, PrintWriter report) {
        double bestTime = Math.min(Arrays.stream(parTimes).min().orElse(seqTime), streamTime);
        double bestSpeedup = seqTime / bestTime;

        double s = (1.0 / bestSpeedup - 1.0 / threads) / (1.0 - 1.0 / threads);

        report.printf("\n=== Итоговый анализ ===\n");
        report.printf("Лучшее время:     %.2f мс\n", bestTime);
        report.printf("Макс. ускорение:  %.2fx\n", bestSpeedup);
        report.printf("Доля посл. кода:  %.2f%% (по закону Амдала)\n", Math.max(0, s * 100));
        report.printf("Теор. максимум:   %.2fx (при бесконечных ядрах)\n", s > 0 ? 1/s : 0);
    }

    private static void runCorrectnessTests(PrintWriter report) {
        report.println("=== Тесты корректности ===");
        int[] test1 = {6, 8, 7, 13, 5, 9, 4};
        int[] test2 = {20319251, 6997901, 6997927};

        boolean t1Seq = new SequentialPrimeChecker().hasNonPrime(test1);
        boolean t1Par = new ParallelThreadPrimeChecker(4).hasNonPrime(test1);
        boolean t1Str = new ParallelStreamPrimeChecker().hasNonPrime(test1);

        report.printf("Тест 1 (ожидается true): Seq=%b, Par=%b, Stream=%b -> %s\n",
                t1Seq, t1Par, t1Str, (t1Seq && t1Par && t1Str) ? "OK" : "FAIL");

        boolean t2Seq = new SequentialPrimeChecker().hasNonPrime(test2);
        boolean t2Par = new ParallelThreadPrimeChecker(4).hasNonPrime(test2);
        boolean t2Str = new ParallelStreamPrimeChecker().hasNonPrime(test2);

        report.printf("Тест 2 (ожидается false): Seq=%b, Par=%b, Stream=%b -> %s\n",
                t2Seq, t2Par, t2Str, (!t2Seq && !t2Par && !t2Str) ? "OK" : "FAIL");
    }
}