package ru.nsu.maltsev.task3;

import java.util.Map;

public class Number extends Expression{
    private final int value;

    public Number(int value){
        this.value = value;
    }

    public int eval(Map<String, Integer> vars){
        return value;
    }

    public Expression derivative(String var){
        return new Number(0);
    }

    public void print(){
        System.out.print(value);
    }

    public Expression simple(){
        return this;
    }

    public int getValue(){
        return value;
    }

    public boolean structuralEquals(Expression other) {
        if (!(other instanceof Number)) {
            return false;
        }
        return this.value == ((Number) other).value;
    }
}
