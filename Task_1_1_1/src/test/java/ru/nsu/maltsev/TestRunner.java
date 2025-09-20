package ru.nsu.maltsev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRunner {

    @Test
    void testEmpty() {
        int[] a = {};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{}, a);
    }

    @Test
    void testSingle() {
        int[] a = {42};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{42}, a);
    }

    @Test
    void testReverse() {
        int[] a = {5,4,3,2,1};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{1,2,3,4,5}, a);
    }

    @Test
    void testSorted() {
        int[] a = {1,2,3,4,5};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{1,2,3,4,5}, a);
    }

    @Test
    void testDuplicates() {
        int[] a = {3,1,2,3,1,2};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{1,1,2,2,3,3}, a);
    }

    @Test
    void testNegatives() {
        int[] a = {-1, -5, 3, 0, -2};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{-5, -2, -1, 0, 3}, a);
    }

    @Test
    void testAllEqual() {
        int[] a = {7, 7, 7, 7, 7};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, a);
    }

    @Test
    void testTwoElements() {
        int[] a = {9, 1};
        Heapsort.heapsort(a);
        assertArrayEquals(new int[]{1, 9}, a);
    }

    @Test
    void testLargeArray() {
        int[] a = new int[1000];
        for (int i = 0; i < a.length; i++) {
            a[i] = (int)(Math.random() * 1000) - 500;
        }
        int[] expected = a.clone();
        java.util.Arrays.sort(expected);

        Heapsort.heapsort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}