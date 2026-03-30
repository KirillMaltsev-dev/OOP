package ru.nsu.maltsev.task_2_1_2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DistributedWorker implements AutoCloseable {
    private final int port;
    private final ExecutorService executorService;
    private volatile boolean running;
    private ServerSocket serverSocket;

    public DistributedWorker(int port) {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be in range 1..65535");
        }
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> handleClient(socket));
            } catch (IOException e) {
                if (running) {
                    throw e;
                }
            }
        }
    }

    private void handleClient(Socket socket) {
        try (Socket client = socket;
             DataInputStream in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()))) {
            client.setSoTimeout(15000);

            String requestType = in.readUTF();
            String taskId = in.readUTF();

            if (!Protocol.CHECK_TASK.equals(requestType)) {
                out.writeUTF(Protocol.ERROR);
                out.writeUTF(taskId);
                out.writeUTF("Unsupported request type");
                out.flush();
                return;
            }

            int length = in.readInt();
            if (length < 0) {
                out.writeUTF(Protocol.ERROR);
                out.writeUTF(taskId);
                out.writeUTF("Negative chunk length");
                out.flush();
                return;
            }

            int[] numbers = new int[length];
            for (int i = 0; i < length; i++) {
                numbers[i] = in.readInt();
            }

            long expectedChecksum = in.readLong();
            long actualChecksum = Protocol.checksum(numbers);
            if (expectedChecksum != actualChecksum) {
                out.writeUTF(Protocol.ERROR);
                out.writeUTF(taskId);
                out.writeUTF("Checksum mismatch");
                out.flush();
                return;
            }

            boolean hasNonPrime = false;
            for (int number : numbers) {
                if (!PrimeChecker.isPrime(number)) {
                    hasNonPrime = true;
                    break;
                }
            }

            out.writeUTF(Protocol.RESULT);
            out.writeUTF(taskId);
            out.writeBoolean(hasNonPrime);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        executorService.shutdownNow();
    }
}