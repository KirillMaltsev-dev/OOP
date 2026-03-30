package ru.nsu.maltsev.task_2_2_1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: <config.json>");
        }
        PizzeriaConfig config = JsonConfigParser.parse(args[0]);
        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.run();
    }
}