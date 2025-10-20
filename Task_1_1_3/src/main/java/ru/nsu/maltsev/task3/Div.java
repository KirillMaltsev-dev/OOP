package ru.nsu.maltsev.task3;

import java.util.Map;

public class Div extends Expression{
    private final Expression left;
    private final Expression right;

    public Div(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    public int eval(Map<String, Integer> vars){
        return left.eval(vars) / right.eval(vars);
    }

    public Expression derivative(String var){
        return new Div(new Sub(new Mul(left.derivative(var), right), new Mul(right.derivative(var), left)), new Mul(right, right));
    }

    public void print(){
        System.out.print("(");
        left.print();
        System.out.print("/");
        right.print();
        System.out.print(")");
    }


    public Expression simple() {
        Expression l = left.simple();
        Expression r = right.simple();

        if (l instanceof Number && r instanceof Number) {
            return new Number(((Number) l).getValue() / ((Number) r).getValue());
        }

        // деление на 1
        if (r instanceof Number && ((Number) r).getValue() == 1) return l;

        // 0 / x → 0
        if (l instanceof Number && ((Number) l).getValue() == 0) return new Number(0);

        return new Div(l, r);
    }

    public boolean structuralEquals(Expression other) {
        if (!(other instanceof Div)) {
            return false;
        }
        Div otherDiv = (Div) other;
        return this.left.structuralEquals(otherDiv.left) &&
                this.right.structuralEquals(otherDiv.right);
    }
}
