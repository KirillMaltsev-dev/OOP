package ru.nsu.maltsev.Task_1_1_3;

import java.util.Map;

public abstract class Expression {
    public abstract int eval(Map<String, Integer> vars);
    public abstract Expression derivative(String var);
    public abstract void print();
    public abstract Expression simple();
    public abstract boolean structuralEquals(Expression other);
}
