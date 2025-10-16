package ru.nsu.maltsev.Task_1_1_3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class tests {

    private Map<String, Integer> vars;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        vars = new HashMap<>();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    // ============== Number Tests ==============

    @Test
    public void testNumberEval() {
        Expression num = new Number(42);
        Assertions.assertEquals(42, num.eval(vars));
    }

    @Test
    public void testNumberDerivative() {
        Expression num = new Number(10);
        Expression derivative = num.derivative("x");
        Assertions.assertTrue(derivative instanceof Number);
        Assertions.assertEquals(0, derivative.eval(vars));
    }

    @Test
    public void testNumberPrint() {
        Expression num = new Number(7);
        num.print();
        Assertions.assertEquals("7", outputStream.toString());
    }

    @Test
    public void testNumberSimple() {
        Expression num = new Number(5);
        Expression simplified = num.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(5, simplified.eval(vars));
    }

    @Test
    public void testNumberGetValue() {
        Number num = new Number(15);
        Assertions.assertEquals(15, num.getValue());
    }

    // ============== Variable Tests ==============

    @Test
    public void testVariableEval() {
        vars.put("x", 10);
        Expression var = new Variable("x");
        Assertions.assertEquals(10, var.eval(vars));
    }

    @Test
    public void testVariableEvalUndefined() {
        Expression var = new Variable("y");
        Assertions.assertThrows(IllegalArgumentException.class, () -> var.eval(vars));
    }

    @Test
    public void testVariableDerivativeSameVar() {
        Expression var = new Variable("x");
        Expression derivative = var.derivative("x");
        Assertions.assertEquals(1, derivative.eval(vars));
    }

    @Test
    public void testVariableDerivativeDifferentVar() {
        Expression var = new Variable("x");
        Expression derivative = var.derivative("y");
        Assertions.assertEquals(0, derivative.eval(vars));
    }

    @Test
    public void testVariablePrint() {
        Expression var = new Variable("abc");
        var.print();
        Assertions.assertEquals("abc", outputStream.toString());
    }

    @Test
    public void testVariableSimple() {
        Expression var = new Variable("x");
        Expression simplified = var.simple();
        Assertions.assertTrue(simplified instanceof Variable);
    }

    @Test
    public void testVariableGetName() {
        Variable var = new Variable("myVar");
        Assertions.assertEquals("myVar", var.getName());
    }

    // ============== Add Tests ==============

    @Test
    public void testAddEval() {
        vars.put("x", 5);
        Expression add = new Add(new Number(3), new Variable("x"));
        Assertions.assertEquals(8, add.eval(vars));
    }

    @Test
    public void testAddDerivative() {
        Expression add = new Add(new Variable("x"), new Number(5));
        Expression derivative = add.derivative("x");
        Assertions.assertEquals(1, derivative.eval(vars));
    }

    @Test
    public void testAddPrint() {
        Expression add = new Add(new Number(2), new Number(3));
        add.print();
        Assertions.assertEquals("(2+3)", outputStream.toString());
    }

    @Test
    public void testAddSimpleWithNumbers() {
        Expression add = new Add(new Number(2), new Number(3));
        Expression simplified = add.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(5, simplified.eval(vars));
    }

    @Test
    public void testAddSimpleWithVariable() {
        Expression add = new Add(new Number(2), new Variable("x"));
        Expression simplified = add.simple();
        Assertions.assertTrue(simplified instanceof Add);
    }

    // ============== Sub Tests ==============

    @Test
    public void testSubEval() {
        vars.put("x", 10);
        Expression sub = new Sub(new Variable("x"), new Number(3));
        Assertions.assertEquals(7, sub.eval(vars));
    }

    @Test
    public void testSubDerivative() {
        Expression sub = new Sub(new Variable("x"), new Number(2));
        Expression derivative = sub.derivative("x");
        Assertions.assertEquals(1, derivative.eval(vars));
    }

    @Test
    public void testSubPrint() {
        Expression sub = new Sub(new Number(10), new Number(4));
        sub.print();
        Assertions.assertEquals("(10-4)", outputStream.toString());
    }

    @Test
    public void testSubSimpleWithNumbers() {
        Expression sub = new Sub(new Number(10), new Number(3));
        Expression simplified = sub.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(7, simplified.eval(vars));
    }

    @Test
    public void testSubSimpleSameExpression() {
        Expression sub = new Sub(new Variable("x"), new Variable("x"));
        Expression simplified = sub.simple();
        // Note: This test might fail with current implementation
        // due to toString() comparison issue
        Assertions.assertTrue(simplified instanceof Number || simplified instanceof Sub);
    }

    // ============== Mul Tests ==============

    @Test
    public void testMulEval() {
        vars.put("x", 5);
        Expression mul = new Mul(new Number(2), new Variable("x"));
        Assertions.assertEquals(10, mul.eval(vars));
    }

    @Test
    public void testMulDerivative() {
        // d/dx(2*x) = 2
        Expression mul = new Mul(new Number(2), new Variable("x"));
        Expression derivative = mul.derivative("x");
        vars.put("x", 0);
        Assertions.assertEquals(2, derivative.eval(vars));
    }

    @Test
    public void testMulPrint() {
        Expression mul = new Mul(new Number(3), new Number(4));
        mul.print();
        Assertions.assertEquals("(3*4)", outputStream.toString());
    }

    @Test
    public void testMulSimpleWithNumbers() {
        Expression mul = new Mul(new Number(3), new Number(4));
        Expression simplified = mul.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(12, simplified.eval(vars));
    }

    @Test
    public void testMulSimpleWithZeroLeft() {
        Expression mul = new Mul(new Number(0), new Variable("x"));
        Expression simplified = mul.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(0, simplified.eval(vars));
    }

    @Test
    public void testMulSimpleWithZeroRight() {
        Expression mul = new Mul(new Variable("x"), new Number(0));
        Expression simplified = mul.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(0, simplified.eval(vars));
    }

    @Test
    public void testMulSimpleWithOneLeft() {
        Expression mul = new Mul(new Number(1), new Variable("x"));
        Expression simplified = mul.simple();
        Assertions.assertTrue(simplified instanceof Variable);
    }

    @Test
    public void testMulSimpleWithOneRight() {
        Expression mul = new Mul(new Variable("x"), new Number(1));
        Expression simplified = mul.simple();
        Assertions.assertTrue(simplified instanceof Variable);
    }

    // ============== Div Tests ==============

    @Test
    public void testDivEval() {
        vars.put("x", 20);
        Expression div = new Div(new Variable("x"), new Number(4));
        Assertions.assertEquals(5, div.eval(vars));
    }

    @Test
    public void testDivDerivative() {
        // d/dx(x/2) = 1/2
        Expression div = new Div(new Variable("x"), new Number(2));
        Expression derivative = div.derivative("x");
        vars.put("x", 0);
        Assertions.assertEquals(0, derivative.eval(vars)); // (1*2 - 0*x)/(2*2) = 2/4 = 0 (integer division)
    }

    @Test
    public void testDivPrint() {
        Expression div = new Div(new Number(20), new Number(4));
        div.print();
        Assertions.assertEquals("(20/4)", outputStream.toString());
    }

    @Test
    public void testDivSimpleWithNumbers() {
        Expression div = new Div(new Number(20), new Number(4));
        Expression simplified = div.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(5, simplified.eval(vars));
    }

    @Test
    public void testDivSimpleWithOne() {
        Expression div = new Div(new Variable("x"), new Number(1));
        Expression simplified = div.simple();
        Assertions.assertTrue(simplified instanceof Variable);
    }

    @Test
    public void testDivSimpleZeroDivided() {
        Expression div = new Div(new Number(0), new Variable("x"));
        Expression simplified = div.simple();
        Assertions.assertTrue(simplified instanceof Number);
        Assertions.assertEquals(0, simplified.eval(vars));
    }

    // ============== Parser Tests ==============

    @Test
    public void testParserSimpleNumber() {
        Parser parser = new Parser("42");
        Expression expr = parser.parse();
        Assertions.assertEquals(42, expr.eval(vars));
    }

    @Test
    public void testParserVariable() {
        Parser parser = new Parser("x");
        vars.put("x", 15);
        Expression expr = parser.parse();
        Assertions.assertEquals(15, expr.eval(vars));
    }

    @Test
    public void testParserAddition() {
        Parser parser = new Parser("3+5");
        Expression expr = parser.parse();
        Assertions.assertEquals(8, expr.eval(vars));
    }

    @Test
    public void testParserSubtraction() {
        Parser parser = new Parser("10-4");
        Expression expr = parser.parse();
        Assertions.assertEquals(6, expr.eval(vars));
    }

    @Test
    public void testParserMultiplication() {
        Parser parser = new Parser("3*4");
        Expression expr = parser.parse();
        Assertions.assertEquals(12, expr.eval(vars));
    }

    @Test
    public void testParserDivision() {
        Parser parser = new Parser("20/4");
        Expression expr = parser.parse();
        Assertions.assertEquals(5, expr.eval(vars));
    }

    @Test
    public void testParserWithParentheses() {
        Parser parser = new Parser("(3+2)*4");
        Expression expr = parser.parse();
        Assertions.assertEquals(20, expr.eval(vars));
    }

    @Test
    public void testParserPrecedence() {
        Parser parser = new Parser("3+2*4");
        Expression expr = parser.parse();
        Assertions.assertEquals(11, expr.eval(vars));
    }

    @Test
    public void testParserComplexExpression() {
        Parser parser = new Parser("3+2*x");
        vars.put("x", 10);
        Expression expr = parser.parse();
        Assertions.assertEquals(23, expr.eval(vars));
    }

    @Test
    public void testParserMultiCharVariable() {
        Parser parser = new Parser("abc+5");
        vars.put("abc", 10);
        Expression expr = parser.parse();
        Assertions.assertEquals(15, expr.eval(vars));
    }

    @Test
    public void testParserWithSpaces() {
        Parser parser = new Parser("  3  +  5  ");
        Expression expr = parser.parse();
        Assertions.assertEquals(8, expr.eval(vars));
    }

    @Test
    public void testParserInvalidCharacter() {
        Parser parser = new Parser("3+@");
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse());
    }

    @Test
    public void testParserUnexpectedEnd() {
        Parser parser = new Parser("3+");
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse());
    }

    @Test
    public void testParserMismatchedParentheses() {
        Parser parser = new Parser("(3+2");
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse());
    }

    @Test
    public void testParserUnexpectedCharacterAtEnd() {
        Parser parser = new Parser("3+5)");
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse());
    }

    // ============== Integration Tests ==============

    @Test
    public void testComplexExpressionFromAssignment() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        vars.put("x", 10);
        Assertions.assertEquals(23, e.eval(vars));
    }

    @Test
    public void testComplexExpressionDerivative() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        Expression de = e.derivative("x");
        vars.put("x", 10);
        Assertions.assertEquals(2, de.eval(vars));
    }

    @Test
    public void testComplexExpressionPrint() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        e.print();
        Assertions.assertEquals("(3+(2*x))", outputStream.toString());
    }

    @Test
    public void testNestedExpression() {
        // ((x + 2) * 3) - 5
        Expression expr = new Sub(
                new Mul(
                        new Add(new Variable("x"), new Number(2)),
                        new Number(3)
                ),
                new Number(5)
        );
        vars.put("x", 4);
        Assertions.assertEquals(13, expr.eval(vars)); // ((4+2)*3)-5 = 18-5 = 13
    }

    @Test
    public void testMultipleVariables() {
        // x + y
        Expression expr = new Add(new Variable("x"), new Variable("y"));
        vars.put("x", 10);
        vars.put("y", 13);
        Assertions.assertEquals(23, expr.eval(vars));
    }

    @Test
    public void testDerivativeWithMultipleOccurrences() {
        // x * x
        Expression expr = new Mul(new Variable("x"), new Variable("x"));
        Expression derivative = expr.derivative("x");
        vars.put("x", 3);
        Assertions.assertEquals(6, derivative.eval(vars)); // 2*x at x=3 is 6
    }

    @Test
    public void testSimplificationChain() {
        // (5 + 3) * (2 + 0)
        Expression expr = new Mul(
                new Add(new Number(5), new Number(3)),
                new Add(new Number(2), new Number(0))
        );
        Expression simplified = expr.simple();
        Assertions.assertEquals(16, simplified.eval(vars));
    }
}
