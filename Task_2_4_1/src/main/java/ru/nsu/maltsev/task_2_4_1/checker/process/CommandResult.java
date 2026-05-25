package ru.nsu.maltsev.task_2_4_1.checker.process;

public class CommandResult {
    private final int exitCode;
    private final String output;
    private final boolean timedOut;

    public CommandResult(int exitCode, String output, boolean timedOut) {
        this.exitCode = exitCode;
        this.output = output;
        this.timedOut = timedOut;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getOutput() {
        return output;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public boolean isSuccess() {
        return exitCode == 0 && !timedOut;
    }
}