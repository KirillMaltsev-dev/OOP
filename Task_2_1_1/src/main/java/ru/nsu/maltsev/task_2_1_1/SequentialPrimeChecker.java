package ru.nsu.maltsev.task_2_1_1;

/**
 * Последовательная реализация проверки непростых чисел
 */
public class SequentialPrimeChecker extends PrimeChecker {

    @Override
    public boolean hasNonPrime(int[] numbers) {
        for (int num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMethodName() {
        return "Sequential";
    }
}