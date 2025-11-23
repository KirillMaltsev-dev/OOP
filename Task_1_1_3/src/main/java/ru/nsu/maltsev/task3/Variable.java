package ru.nsu.maltsev.task3;

import java.util.Map;

public class Variable implements Expression{
    private final String name;

    public Variable(String name){
        this.name = name;
    }

    public int eval(Map <String, Integer> vars){
        if (!vars.containsKey(name)) {
            throw new IllegalArgumentException("Variable not defined: " + name);
        }
        return vars.get(name);
    }

    public Expression derivative(String var){
        if (name.equals(var)){
            return new Number(1);
        }
        else{
            return new Number(0);
        }
    }
    public void print(){
        System.out.print(name);
    }

    public Expression simple(){
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean structuralEquals(Expression other) {
        if (!(other instanceof Variable)) {
            return false;
        }
        return this.name.equals(((Variable) other).name);
    }
}
