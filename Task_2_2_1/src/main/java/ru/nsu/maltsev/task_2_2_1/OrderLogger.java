package ru.nsu.maltsev.task_2_2_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OrderLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogger.class);

    private OrderLogger() {
    }

    public static void log(int orderId, OrderState state) {
        LOGGER.info("{} {}", orderId, state.name());
    }
}