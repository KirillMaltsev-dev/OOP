package ru.nsu.maltsev.task_2_1_2;

import java.io.IOException;

public class WorkerMain {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: <port>");
        }
        int port = Integer.parseInt(args[0]);
        DistributedWorker worker = new DistributedWorker(port);
        worker.start();
    }
}