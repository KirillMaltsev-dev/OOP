package ru.nsu.maltsev.task_2_1_2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DistributedPrimeChecker extends PrimeChecker {
    private final List<WorkerEndpoint> workers;
    private final int chunkSize;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;

    public DistributedPrimeChecker(List<WorkerEndpoint> workers, int chunkSize, int connectTimeoutMs, int readTimeoutMs) {
        if (workers == null || workers.isEmpty()) {
            throw new IllegalArgumentException("At least one worker is required");
        }
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Chunk size must be >= 1");
        }
        if (connectTimeoutMs < 1 || readTimeoutMs < 1) {
            throw new IllegalArgumentException("Timeouts must be >= 1");
        }
        this.workers = List.copyOf(workers);
        this.chunkSize = chunkSize;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
    }

    @Override
    public boolean hasNonPrime(int[] numbers) {
        if (numbers.length == 0) {
            return false;
        }

        List<ChunkTask> tasks = split(numbers);
        LinkedBlockingQueue<ChunkTask> queue = new LinkedBlockingQueue<>(tasks);
        AtomicInteger remainingTasks = new AtomicInteger(tasks.size());
        AtomicBoolean nonPrimeFound = new AtomicBoolean(false);
        AtomicReference<RuntimeException> failure = new AtomicReference<>();
        List<Thread> threads = new ArrayList<>();

        for (WorkerEndpoint worker : workers) {
            Thread thread = new Thread(() -> processWorker(worker, queue, remainingTasks, nonPrimeFound, failure));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for workers", e);
            }
        }

        if (failure.get() != null) {
            throw failure.get();
        }
        if (!nonPrimeFound.get() && remainingTasks.get() > 0) {
            throw new IllegalStateException("Unable to complete computation: no reachable workers left");
        }
        return nonPrimeFound.get();
    }

    @Override
    public String getMethodName() {
        return "Distributed";
    }

    private List<ChunkTask> split(int[] numbers) {
        String jobId = UUID.randomUUID().toString();
        List<ChunkTask> tasks = new ArrayList<>();
        int index = 0;
        for (int start = 0; start < numbers.length; start += chunkSize) {
            int end = Math.min(start + chunkSize, numbers.length);
            int[] chunk = Arrays.copyOfRange(numbers, start, end);
            tasks.add(new ChunkTask(jobId + "-" + index, chunk));
            index++;
        }
        return tasks;
    }

    private void processWorker(
            WorkerEndpoint worker,
            LinkedBlockingQueue<ChunkTask> queue,
            AtomicInteger remainingTasks,
            AtomicBoolean nonPrimeFound,
            AtomicReference<RuntimeException> failure
    ) {
        while (!nonPrimeFound.get() && failure.get() == null) {
            if (remainingTasks.get() == 0) {
                return;
            }

            ChunkTask task;
            try {
                task = queue.poll(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failure.compareAndSet(null, new IllegalStateException("Interrupted while polling tasks", e));
                return;
            }

            if (task == null) {
                continue;
            }

            if (nonPrimeFound.get() || failure.get() != null) {
                queue.offer(task);
                return;
            }

            try {
                boolean hasNonPrime = executeTask(worker, task);
                if (hasNonPrime) {
                    nonPrimeFound.set(true);
                    return;
                }
                remainingTasks.decrementAndGet();
            } catch (IOException e) {
                queue.offer(task);
                return;
            }
        }
    }

    private boolean executeTask(WorkerEndpoint worker, ChunkTask task) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(worker.getHost(), worker.getPort()), connectTimeoutMs);
            socket.setSoTimeout(readTimeoutMs);

            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
                out.writeUTF(Protocol.CHECK_TASK);
                out.writeUTF(task.getTaskId());
                out.writeInt(task.getNumbers().length);
                for (int value : task.getNumbers()) {
                    out.writeInt(value);
                }
                out.writeLong(Protocol.checksum(task.getNumbers()));
                out.flush();

                String responseType = in.readUTF();
                String responseTaskId = in.readUTF();
                if (!task.getTaskId().equals(responseTaskId)) {
                    throw new IOException("Unexpected task id from worker " + worker);
                }
                if (Protocol.RESULT.equals(responseType)) {
                    return in.readBoolean();
                }
                if (Protocol.ERROR.equals(responseType)) {
                    String errorMessage = in.readUTF();
                    throw new IOException(errorMessage);
                }
                throw new IOException("Unknown response from worker " + worker);
            }
        }
    }
}