package ru.nsu.maltsev.task3;

import java.util.Map;

public class Add extends Expression{
    private final Expression left;
    private final Expression right;

    public Add(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    public int eval(Map<String, Integer> vars){
        return left.eval(vars) + right.eval(vars);
    }

    public Expression derivative(String var){
        return new Add(left.derivative(var), right.derivative(var));
    }

    public void print(){
        System.out.print("(");
        left.print();
        System.out.print("+");
        right.print();
        System.out.print(")");
    }

    public Expression simple(){
        Expression l = left.simple();
        Expression r = right.simple();
        if (l instanceof Number && r instanceof Number){
            return new Number(((Number) l).getValue() + ((Number) r).getValue());
        }
        return new Add(l, r);
    }

    public boolean structuralEquals(Expression other) {
        if (!(other instanceof Add)) {
            return false;
        }
        Add otherAdd = (Add) other;
        return this.left.structuralEquals(otherAdd.left) &&
                this.right.structuralEquals(otherAdd.right);
    }
}
