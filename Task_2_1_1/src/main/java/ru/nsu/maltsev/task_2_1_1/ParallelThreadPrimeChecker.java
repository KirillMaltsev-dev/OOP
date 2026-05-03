package ru.nsu.maltsev.task_2_1_1;

public class ParallelThreadPrimeChecker extends PrimeChecker {

    private final int threadCount;

    /**
     * Конструктор
     * @param threadCount количество потоков для параллельной обработки
     */
    public ParallelThreadPrimeChecker(int threadCount) {
        if (threadCount < 1) {
            throw new IllegalArgumentException("Количество потоков должно быть >= 1");
        }
        this.threadCount = threadCount;
    }

    @Override
    public boolean hasNonPrime(int[] numbers) {
        if (numbers.length == 0) return false;

        final int chunkSize = (numbers.length + threadCount - 1) / threadCount;
        final SharedResult result = new SharedResult();
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, numbers.length);

            if (start >= numbers.length) break;

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (result.hasNonPrime()) return;
                    if (!isPrime(numbers[j])) {
                        result.setNonPrimeFound(true);
                        return;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return result.hasNonPrime();
    }

    @Override
    public String getMethodName() {
        return "Parallel (Thread, " + threadCount + " threads)";
    }

    /**
     * Класс для хранения общего результата между потоками
     */
    private static class SharedResult {
        private volatile boolean nonPrimeFound = false;

        public synchronized void setNonPrimeFound(boolean value) {
            this.nonPrimeFound = value;
        }

        public boolean hasNonPrime() {
            return nonPrimeFound;
        }
    }
}
