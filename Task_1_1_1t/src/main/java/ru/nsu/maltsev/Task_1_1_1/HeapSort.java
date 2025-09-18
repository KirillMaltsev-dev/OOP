package org.example;

public class Heapsort {
    public static void heapsort(int[] a) {
        if (a == null || a.length < 2) return;
        int n = a.length;

        for (int i = (n / 2) - 1; i >= 0; i--) {
            siftDown(a, i, n);
        }

        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end);
            siftDown(a, 0, end);
        }
    }

    private static void siftDown(int[] a, int root, int size) {
        int val = a[root];
        int idx = root;
        while (true) {
            int left = 2 * idx + 1;
            if (left >= size) break;
            int right = left + 1;
            int largest = left;
            if (right < size && a[right] > a[left]) largest = right;
            if (a[largest] <= val) break;
            a[idx] = a[largest];
            idx = largest;
        }
        a[idx] = val;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
