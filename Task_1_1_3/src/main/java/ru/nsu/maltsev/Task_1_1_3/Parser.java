package ru.nsu.maltsev.Task_1_1_3;

public class Parser {
    private final String input;
    private int pos;

    public Parser(String input) {
        this.input = input.replaceAll("\\s+", ""); // убрать пробелы
        this.pos = 0;
    }

    public Expression parse() {
        Expression result = parseExpression();
        if (pos != input.length()) {
            throw new IllegalArgumentException("Unexpected character at position " + pos + ": " + peek());
        }
        return result;
    }

    //Сумма и разность
    private Expression parseExpression() {
        Expression left = parseTerm();
        while (true) {
            char op = peek();
            if (op == '+' || op == '-') {
                consume();
                Expression right = parseTerm();
                if (op == '+') {
                    left = new Add(left, right);
                } else {
                    left = new Sub(left, right);
                }
            } else {
                break;
            }
        }
        return left;
    }

    // Умножение и деление
    private Expression parseTerm() {
        Expression left = parseFactor();
        while (true) {
            char op = peek();
            if (op == '*' || op == '/') {
                consume();
                Expression right = parseFactor();
                if (op == '*') {
                    left = new Mul(left, right);
                } else {
                    left = new Div(left, right);
                }
            } else {
                break;
            }
        }
        return left;
    }

    // Число, переменная или выражение в скобках
    private Expression parseFactor() {
        char c = peek();
        if (c == '(') {
            consume();
            Expression expr = parseExpression();
            expect(')');
            return expr;
        } else if (Character.isDigit(c)) {
            return parseNumber();
        } else if (Character.isLetter(c)) {
            return parseVariable();
        } else {
            throw new IllegalArgumentException("Unexpected character: " + c);
        }
    }

    private Expression parseNumber() {
        int start = pos;
        while (Character.isDigit(peek())) consume();
        int value = Integer.parseInt(input.substring(start, pos));
        return new Number(value);
    }

    private Expression parseVariable() {
        int start = pos;
        while (Character.isLetter(peek())) consume();
        String name = input.substring(start, pos);
        return new Variable(name);
    }

    private char peek() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos);
    }

    private void consume() {
        pos++;
    }

    private void expect(char expected) {
        if (peek() != expected) {
            throw new IllegalArgumentException("Expected '" + expected + "', found '" + peek() + "'");
        }
        pos++;
    }
}
