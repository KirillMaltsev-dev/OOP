package ru.nsu.maltsev.task_2_1_2;

import java.util.Objects;

public class WorkerEndpoint {
    private final String host;
    private final int port;

    public WorkerEndpoint(String host, int port) {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Host must not be blank");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be in range 1..65535");
        }
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public static WorkerEndpoint parse(String value) {
        int separatorIndex = value.lastIndexOf(':');
        if (separatorIndex <= 0 || separatorIndex == value.length() - 1) {
            throw new IllegalArgumentException("Worker must be in host:port format: " + value);
        }
        String host = value.substring(0, separatorIndex).trim();
        int port = Integer.parseInt(value.substring(separatorIndex + 1).trim());
        return new WorkerEndpoint(host, port);
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WorkerEndpoint)) {
            return false;
        }
        WorkerEndpoint endpoint = (WorkerEndpoint) other;
        return port == endpoint.port && host.equals(endpoint.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}