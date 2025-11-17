package ru.nsu.maltsev.task3;

import java.util.Map;

public abstract class Expression {
    public abstract int eval(Map<String, Integer> vars);
    public abstract Expression derivative(String var);
    public abstract void print();
    public abstract Expression simplify();
    public abstract boolean structuralEquals(Expression other);
}
