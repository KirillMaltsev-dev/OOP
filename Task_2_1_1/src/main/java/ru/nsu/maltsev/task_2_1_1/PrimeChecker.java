package ru.nsu.maltsev.task_2_1_1;

public abstract class PrimeChecker {

    /**
     * Проверяет, является ли число простым
     * @param n число для проверки
     * @return true если число простое, false иначе
     */
    protected static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Проверяет наличие непростых чисел в массиве
     * @param numbers массив чисел для проверки
     * @return true если есть хотя бы одно непростое число
     */
    public abstract boolean hasNonPrime(int[] numbers);

    /**
     * Возвращает название метода
     */
    public abstract String getMethodName();
}
