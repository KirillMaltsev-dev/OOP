package ru.nsu.maltsev.task3;

import java.util.Map;

public class Mul implements Expression{
    private final Expression left;
    private final Expression right;

    public Mul(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    public int eval(Map<String, Integer> vars){
        return left.eval(vars) * right.eval(vars);
    }

    public Expression derivative(String var){
        return new Add(new Mul(left.derivative(var), right), new Mul(right.derivative(var), left));
    }

    public void print(){
        System.out.print("(");
        left.print();
        System.out.print("*");
        right.print();
        System.out.print(")");
    }

    public Expression simple(){
        Expression l = left.simple();
        Expression r = right.simple();

        if (l instanceof Number && r instanceof Number) {
            return new Number(((Number) l).getValue() * ((Number) r).getValue());
        }

        if (l instanceof Number && ((Number) l).getValue() == 0) return new Number(0);
        if (r instanceof Number && ((Number) r).getValue() == 0) return new Number(0);

        if (l instanceof Number && ((Number) l).getValue() == 1) return r;
        if (r instanceof Number && ((Number) r).getValue() == 1) return l;

        return new Mul(l, r);
    }

    public boolean structuralEquals(Expression other) {
        if (!(other instanceof Mul)) {
            return false;
        }
        Mul otherMul = (Mul) other;
        return this.left.structuralEquals(otherMul.left) &&
                this.right.structuralEquals(otherMul.right);
    }
}
