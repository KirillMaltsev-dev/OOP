package ru.nsu.maltsev.task1;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        Heapsort.heapsort(arr);
        System.out.println(Arrays.toString(arr));
    }
}