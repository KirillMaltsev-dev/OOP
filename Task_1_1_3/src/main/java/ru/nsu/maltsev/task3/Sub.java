package ru.nsu.maltsev.task3;

import java.util.Map;

public class Sub extends Expression{
    private final Expression left;
    private final Expression right;

    public Sub(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    public int eval(Map<String, Integer> vars){
        return left.eval(vars) - right.eval(vars);
    }

    public Expression derivative(String var){
        return new Sub(left.derivative(var), right.derivative(var));
    }

    public void print(){
        System.out.print("(");
        left.print();
        System.out.print("-");
        right.print();
        System.out.print(")");
    }

    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();

        if (l instanceof Number && r instanceof Number) {
            return new Number(((Number) l).getValue() - ((Number) r).getValue());
        }

        if (l.structuralEquals(r)) {
            return new Number(0);
        }

        return new Sub(l, r);
    }

    public boolean structuralEquals(Expression other) {
        if (!(other instanceof Sub)) {
            return false;
        }
        Sub otherSub = (Sub) other;
        return this.left.structuralEquals(otherSub.left) &&
                this.right.structuralEquals(otherSub.right);
    }
}
