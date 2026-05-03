package ru.nsu.maltsev.task_2_1_2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: <chunkSize> <host1:port1,host2:port2,...> <n1,n2,n3,...>");
        }

        int chunkSize = Integer.parseInt(args[0]);
        List<WorkerEndpoint> workers = parseWorkers(args[1]);
        int[] numbers = parseNumbers(args[2]);

        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, chunkSize, 3000, 15000);
        boolean result = checker.hasNonPrime(numbers);
        System.out.println(result);
    }

    private static List<WorkerEndpoint> parseWorkers(String value) {
        String[] parts = value.split(",");
        List<WorkerEndpoint> workers = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                workers.add(WorkerEndpoint.parse(trimmed));
            }
        }
        if (workers.isEmpty()) {
            throw new IllegalArgumentException("At least one worker must be specified");
        }
        return workers;
    }

    private static int[] parseNumbers(String value) {
        String[] parts = value.split(",");
        List<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                numbers.add(Integer.parseInt(trimmed));
            }
        }
        int[] result = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            result[i] = numbers.get(i);
        }
        return result;
    }
}