package ru.nsu.maltsev.task_2_1_1;

import java.util.Arrays;

/**
 * Параллельная реализация с использованием parallelStream()
 */
public class ParallelStreamPrimeChecker extends PrimeChecker {

    @Override
    public boolean hasNonPrime(int[] numbers) {
        return Arrays.stream(numbers)
                .parallel()
                .anyMatch(n -> !isPrime(n));
    }

    @Override
    public String getMethodName() {
        return "ParallelStream";
    }
}