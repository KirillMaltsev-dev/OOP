package ru.nsu.maltsev.Task_1_1_3;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Write the expression: ");
        String input = scanner.nextLine();

        Parser parser = new Parser(input);
        Expression e = parser.parse();

        System.out.print("expression: ");
        e.print();
        System.out.println();

        Expression simplified = e.simple();
        System.out.print("simplified expression: ");
        simplified.print();
        System.out.println();

        System.out.print("Enter a variable to differentiate: ");
        String var = scanner.nextLine();

        Expression de = e.derivative(var);
        System.out.print("derivative: ");
        de.print();
        System.out.println();

        Expression simplifiedDe = de.simple();
        System.out.print("simplified derivative: ");
        simplifiedDe.print();
        System.out.println();

        System.out.print("Enter the values of the variables via ; for example, x=10;y=13 : ");
        String values = scanner.nextLine();

        Map<String, Integer> vars = parseVars(values);

        int result = simplified.eval(vars);
        System.out.println("The value for the specified variables: " + result);
    }

    public static Map<String, Integer> parseVars(String s) {
        String[] assignments = s.split(";");
        Map<String, Integer> map = new java.util.HashMap<>();
        for (String assign : assignments) {
            String[] parts = assign.split("=");
            String name = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());
            map.put(name, value);
        }
        return map;
    }
}