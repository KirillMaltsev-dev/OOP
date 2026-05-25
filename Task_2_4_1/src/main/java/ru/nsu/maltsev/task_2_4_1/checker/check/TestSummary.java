package ru.nsu.maltsev.task_2_4_1.checker.check;

public class TestSummary {
    private int total;
    private int passed;
    private int failed;
    private int skipped;

    public int getTotal() {
        return total;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getSkipped() {
        return skipped;
    }

    public void add(int total, int failed, int skipped) {
        this.total += total;
        this.failed += failed;
        this.skipped += skipped;
        this.passed += Math.max(0, total - failed - skipped);
    }
}