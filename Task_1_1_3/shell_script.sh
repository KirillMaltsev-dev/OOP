#!/bin/bash

rm -rf build docs app.jar
mkdir -p build docs

# Компиляция исходных файлов
javac -d build src/main/java/ru/nsu/maltsev/Task_1_1_3/Expression.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Number.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Variable.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Add.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Sub.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Mul.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Div.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Parser.java \
              src/main/java/ru/nsu/maltsev/Task_1_1_3/Main.java

# Генерация Javadoc
javadoc -d docs src/main/java/ru/nsu/maltsev/Task_1_1_3/Expression.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Number.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Variable.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Add.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Sub.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Mul.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Div.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Parser.java \
                src/main/java/ru/nsu/maltsev/Task_1_1_3/Main.java

# Создание JAR-файла
jar cfe app.jar ru.nsu.maltsev.Task_1_1_3.Main -C build .

# Запуск приложения из JAR-файла
java -jar app.jar
