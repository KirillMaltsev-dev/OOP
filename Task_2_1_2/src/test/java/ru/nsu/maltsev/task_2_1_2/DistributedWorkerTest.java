package ru.nsu.maltsev.task_2_1_2;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class DistributedWorkerTest {

    @Test
    void workerReturnsTrueWhenChunkContainsNonPrime() throws Exception {
        int port = findFreePort();
        DistributedWorker worker = new DistributedWorker(port);
        Thread serverThread = startWorker(worker);

        try {
            WorkerResponse response = sendCheckTask(port, "task-1", new int[]{2, 3, 4, 5}, Protocol.checksum(new int[]{2, 3, 4, 5}));
            assertEquals(Protocol.RESULT, response.type);
            assertEquals("task-1", response.taskId);
            assertTrue(response.hasNonPrime);
        } finally {
            stopWorker(worker, serverThread);
        }
    }

    @Test
    void workerReturnsFalseWhenAllNumbersArePrime() throws Exception {
        int port = findFreePort();
        DistributedWorker worker = new DistributedWorker(port);
        Thread serverThread = startWorker(worker);

        try {
            WorkerResponse response = sendCheckTask(port, "task-2", new int[]{2, 3, 5, 7, 11}, Protocol.checksum(new int[]{2, 3, 5, 7, 11}));
            assertEquals(Protocol.RESULT, response.type);
            assertEquals("task-2", response.taskId);
            assertFalse(response.hasNonPrime);
        } finally {
            stopWorker(worker, serverThread);
        }
    }

    @Test
    void workerReturnsErrorForUnsupportedRequestType() throws Exception {
        int port = findFreePort();
        DistributedWorker worker = new DistributedWorker(port);
        Thread serverThread = startWorker(worker);

        try {
            try (Socket socket = new Socket("localhost", port);
                 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
                out.writeUTF("BAD_REQUEST");
                out.writeUTF("task-3");
                out.flush();

                String type = in.readUTF();
                String taskId = in.readUTF();
                String message = in.readUTF();

                assertEquals(Protocol.ERROR, type);
                assertEquals("task-3", taskId);
                assertEquals("Unsupported request type", message);
            }
        } finally {
            stopWorker(worker, serverThread);
        }
    }

    @Test
    void workerReturnsErrorForChecksumMismatch() throws Exception {
        int port = findFreePort();
        DistributedWorker worker = new DistributedWorker(port);
        Thread serverThread = startWorker(worker);

        try {
            try (Socket socket = new Socket("localhost", port);
                 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
                out.writeUTF(Protocol.CHECK_TASK);
                out.writeUTF("task-4");
                out.writeInt(3);
                out.writeInt(2);
                out.writeInt(3);
                out.writeInt(5);
                out.writeLong(12345L);
                out.flush();

                String type = in.readUTF();
                String taskId = in.readUTF();
                String message = in.readUTF();

                assertEquals(Protocol.ERROR, type);
                assertEquals("task-4", taskId);
                assertEquals("Checksum mismatch", message);
            }
        } finally {
            stopWorker(worker, serverThread);
        }
    }

    @Test
    void workerReturnsErrorForNegativeChunkLength() throws Exception {
        int port = findFreePort();
        DistributedWorker worker = new DistributedWorker(port);
        Thread serverThread = startWorker(worker);

        try {
            try (Socket socket = new Socket("localhost", port);
                 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
                out.writeUTF(Protocol.CHECK_TASK);
                out.writeUTF("task-5");
                out.writeInt(-1);
                out.flush();

                String type = in.readUTF();
                String taskId = in.readUTF();
                String message = in.readUTF();

                assertEquals(Protocol.ERROR, type);
                assertEquals("task-5", taskId);
                assertEquals("Negative chunk length", message);
            }
        } finally {
            stopWorker(worker, serverThread);
        }
    }

    private static Thread startWorker(DistributedWorker worker) {
        Thread thread = new Thread(() -> {
            try {
                worker.start();
            } catch (IOException e) {
                fail(e);
            }
        });
        thread.start();
        waitForPort(workerPort(worker));
        return thread;
    }

    private static void stopWorker(DistributedWorker worker, Thread thread) throws Exception {
        worker.close();
        thread.join(2000);
    }

    private static int workerPort(DistributedWorker worker) {
        try {
            java.lang.reflect.Field portField = DistributedWorker.class.getDeclaredField("port");
            portField.setAccessible(true);
            return portField.getInt(worker);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void waitForPort(int port) {
        long deadline = System.currentTimeMillis() + 3000;
        while (System.currentTimeMillis() < deadline) {
            try (Socket ignored = new Socket("localhost", port)) {
                return;
            } catch (IOException e) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(interruptedException);
                }
            }
        }
        throw new IllegalStateException("Port did not open in time");
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static WorkerResponse sendCheckTask(int port, String taskId, int[] numbers, long checksum) throws IOException {
        try (Socket socket = new Socket("localhost", port);
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
            out.writeUTF(Protocol.CHECK_TASK);
            out.writeUTF(taskId);
            out.writeInt(numbers.length);
            for (int number : numbers) {
                out.writeInt(number);
            }
            out.writeLong(checksum);
            out.flush();

            WorkerResponse response = new WorkerResponse();
            response.type = in.readUTF();
            response.taskId = in.readUTF();
            if (Protocol.RESULT.equals(response.type)) {
                response.hasNonPrime = in.readBoolean();
            } else {
                response.errorMessage = in.readUTF();
            }
            return response;
        }
    }

    private static class WorkerResponse {
        private String type;
        private String taskId;
        private boolean hasNonPrime;
        private String errorMessage;
    }
}