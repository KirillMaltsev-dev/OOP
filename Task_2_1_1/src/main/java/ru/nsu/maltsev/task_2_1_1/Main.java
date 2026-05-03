package ru.nsu.maltsev.task_2_1_1;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Main {

    private static final int[] THREAD_CONFIGS = {1, 2, 4, 8};
    private static final int PARALLEL_STREAM_PARALLELISM = 8;
    private static final int WARMUP_RUNS = 2;
    private static final int MEASURE_RUNS = 5;
    private static final double T_CRITICAL_90_DF4 = 2.132;

    private static final Path RESULTS_DIR = Path.of("benchmark_results");
    private static final Path DIAGRAM_DIR = Path.of("benchmark_diagram");
    private static final Path REPORT_FILE = RESULTS_DIR.resolve("benchmark_report.txt");

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(PARALLEL_STREAM_PARALLELISM));

        try {
            Files.createDirectories(RESULTS_DIR);
            Files.createDirectories(DIAGRAM_DIR);

            try (PrintWriter reportWriter = new PrintWriter(Files.newBufferedWriter(REPORT_FILE, StandardCharsets.UTF_8))) {
                writeReportHeader(reportWriter);
                runCorrectnessTests(reportWriter);

                Map<String, int[]> scenarios = prepareScenarios();

                for (Map.Entry<String, int[]> entry : scenarios.entrySet()) {
                    benchmarkScenario(entry.getKey(), entry.getValue(), reportWriter);
                }
            }

            System.out.println("Benchmark completed");
            System.out.println("Results directory: " + RESULTS_DIR.toAbsolutePath());
            System.out.println("Diagram directory: " + DIAGRAM_DIR.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write benchmark files", e);
        }
    }

    private static Map<String, int[]> prepareScenarios() {
        Map<String, int[]> scenarios = new LinkedHashMap<>();
        scenarios.put("small_small", generateSmallPrimes(300));
        scenarios.put("large_small", generateSmallPrimes(5000));
        scenarios.put("small_large", generateLargePrimes(300));
        scenarios.put("large_large", generateLargePrimes(1500));
        return scenarios;
    }

    private static void benchmarkScenario(String scenarioName, int[] data, PrintWriter reportWriter) throws IOException {
        Path rawFile = RESULTS_DIR.resolve(scenarioName + "_raw.csv");
        Path summaryFile = RESULTS_DIR.resolve(scenarioName + "_summary.csv");

        warmUp(data);

        try (PrintWriter rawWriter = new PrintWriter(Files.newBufferedWriter(rawFile, StandardCharsets.UTF_8));
             PrintWriter summaryWriter = new PrintWriter(Files.newBufferedWriter(summaryFile, StandardCharsets.UTF_8))) {

            rawWriter.println("Scenario,Algorithm,Run,Time_ms");
            summaryWriter.println("Algorithm,Avg_ms,Min_ms,Max_ms,CI90_ms,Speedup");

            reportWriter.println();
            reportWriter.println("=== " + scenarioName + " ===");
            reportWriter.println("Array size: " + data.length);

            BenchmarkStats seqStats = benchmarkAlgorithm(
                    scenarioName,
                    "Sequential",
                    new SequentialPrimeChecker(),
                    data,
                    rawWriter
            );
            writeSummary(summaryWriter, seqStats, 1.0);
            writeReport(reportWriter, seqStats, 1.0);

            for (int threadCount : THREAD_CONFIGS) {
                BenchmarkStats threadStats = benchmarkAlgorithm(
                        scenarioName,
                        "Threaded " + threadCount,
                        new ParallelThreadPrimeChecker(threadCount),
                        data,
                        rawWriter
                );
                double speedup = seqStats.getAverageMs() / threadStats.getAverageMs();
                writeSummary(summaryWriter, threadStats, speedup);
                writeReport(reportWriter, threadStats, speedup);
            }

            BenchmarkStats streamStats = benchmarkAlgorithm(
                    scenarioName,
                    "Parallel Stream",
                    new ParallelStreamPrimeChecker(),
                    data,
                    rawWriter
            );
            double streamSpeedup = seqStats.getAverageMs() / streamStats.getAverageMs();
            writeSummary(summaryWriter, streamStats, streamSpeedup);
            writeReport(reportWriter, streamStats, streamSpeedup);
        }
    }

    private static BenchmarkStats benchmarkAlgorithm(
            String scenarioName,
            String algorithmName,
            PrimeChecker checker,
            int[] data,
            PrintWriter rawWriter
    ) {
        double[] times = new double[MEASURE_RUNS];

        for (int i = 0; i < MEASURE_RUNS; i++) {
            times[i] = measureMillis(() -> checker.hasNonPrime(data));
            rawWriter.printf(Locale.US, "%s,%s,%d,%.3f%n", scenarioName, algorithmName, i + 1, times[i]);
        }

        return calculateStats(algorithmName, times);
    }

    private static void warmUp(int[] data) {
        PrimeChecker[] checkers = new PrimeChecker[]{
                new SequentialPrimeChecker(),
                new ParallelThreadPrimeChecker(1),
                new ParallelThreadPrimeChecker(2),
                new ParallelThreadPrimeChecker(4),
                new ParallelThreadPrimeChecker(8),
                new ParallelStreamPrimeChecker()
        };

        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (PrimeChecker checker : checkers) {
                checker.hasNonPrime(data);
            }
        }
    }

    private static BenchmarkStats calculateStats(String algorithmName, double[] times) {
        double sum = 0.0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double time : times) {
            sum += time;
            min = Math.min(min, time);
            max = Math.max(max, time);
        }

        double average = sum / times.length;
        double varianceSum = 0.0;

        for (double time : times) {
            double diff = time - average;
            varianceSum += diff * diff;
        }

        double std = times.length > 1 ? Math.sqrt(varianceSum / (times.length - 1)) : 0.0;
        double ci90 = T_CRITICAL_90_DF4 * std / Math.sqrt(times.length);

        return new BenchmarkStats(algorithmName, average, min, max, ci90);
    }

    private static double measureMillis(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000.0;
    }

    private static void writeSummary(PrintWriter summaryWriter, BenchmarkStats stats, double speedup) {
        summaryWriter.printf(
                Locale.US,
                "%s,%.3f,%.3f,%.3f,%.3f,%.3f%n",
                stats.getAlgorithmName(),
                stats.getAverageMs(),
                stats.getMinMs(),
                stats.getMaxMs(),
                stats.getCi90Ms(),
                speedup
        );
    }

    private static void writeReport(PrintWriter reportWriter, BenchmarkStats stats, double speedup) {
        reportWriter.printf(
                Locale.US,
                "%-16s avg = %8.3f ms | min = %8.3f ms | max = %8.3f ms | ci90 = %8.3f ms | speedup = %6.3f%n",
                stats.getAlgorithmName(),
                stats.getAverageMs(),
                stats.getMinMs(),
                stats.getMaxMs(),
                stats.getCi90Ms(),
                speedup
        );
    }

    private static void writeReportHeader(PrintWriter reportWriter) {
        reportWriter.println("Task_2_1_1 benchmark report");
        reportWriter.println("Warmup runs: " + WARMUP_RUNS);
        reportWriter.println("Measure runs: " + MEASURE_RUNS);
        reportWriter.println("Threaded configs: " + Arrays.toString(THREAD_CONFIGS));
        reportWriter.println("ParallelStream parallelism: " + PARALLEL_STREAM_PARALLELISM);
    }

    private static void runCorrectnessTests(PrintWriter reportWriter) {
        reportWriter.println();
        reportWriter.println("=== Correctness tests ===");

        int[] test1 = {6, 8, 7, 13, 5, 9, 4};
        int[] test2 = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967};

        boolean seq1 = new SequentialPrimeChecker().hasNonPrime(test1);
        boolean thr1 = new ParallelThreadPrimeChecker(4).hasNonPrime(test1);
        boolean str1 = new ParallelStreamPrimeChecker().hasNonPrime(test1);

        boolean seq2 = new SequentialPrimeChecker().hasNonPrime(test2);
        boolean thr2 = new ParallelThreadPrimeChecker(4).hasNonPrime(test2);
        boolean str2 = new ParallelStreamPrimeChecker().hasNonPrime(test2);

        reportWriter.println("Test 1 expected true  -> " + (seq1 && thr1 && str1));
        reportWriter.println("Test 2 expected false -> " + (!seq2 && !thr2 && !str2));
    }

    private static int[] generateSmallPrimes(int count) {
        int[] primes = new int[count];
        int found = 0;
        int number = 2;

        while (found < count) {
            if (PrimeChecker.isPrime(number)) {
                primes[found++] = number;
            }
            number++;
        }

        return primes;
    }

    private static int[] generateLargePrimes(int count) {
        int[] primes = new int[count];
        int found = 0;
        int number = 10_000_000;

        while (found < count) {
            if (PrimeChecker.isPrime(number)) {
                primes[found++] = number;
            }
            number++;
        }

        return primes;
    }

    private static final class BenchmarkStats {
        private final String algorithmName;
        private final double averageMs;
        private final double minMs;
        private final double maxMs;
        private final double ci90Ms;

        private BenchmarkStats(String algorithmName, double averageMs, double minMs, double maxMs, double ci90Ms) {
            this.algorithmName = algorithmName;
            this.averageMs = averageMs;
            this.minMs = minMs;
            this.maxMs = maxMs;
            this.ci90Ms = ci90Ms;
        }

        public String getAlgorithmName() {
            return algorithmName;
        }

        public double getAverageMs() {
            return averageMs;
        }

        public double getMinMs() {
            return minMs;
        }

        public double getMaxMs() {
            return maxMs;
        }

        public double getCi90Ms() {
            return ci90Ms;
        }
    }
}