package ru.nsu.maltsev.task_2_2_1;

public enum OrderState {
    RECEIVED,
    TAKEN_BY_BAKER,
    BAKING,
    BAKED,
    STORED,
    TAKEN_BY_COURIER,
    DELIVERING,
    DELIVERED
}