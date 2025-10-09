#!/bin/bash

rm -rf build docs app.jar
mkdir -p build docs

# Компиляция исходных файлов
javac -d build src/main/java/ru/nsu/maltsev/Task_1_1_2/Player.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Main.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Dealer.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Deck.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Card.java

# Генерация Javadoc
javadoc -d docs src/main/java/ru/nsu/maltsev/Task_1_1_2/Player.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Main.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Dealer.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Deck.java src/main/java/ru/nsu/maltsev/Task_1_1_2/Card.java

# Создание JAR-файла
jar cfe app.jar ru.nsu.maltsev.Task_1_1_2.Main -C build .

# Запуск приложения из JAR-файла
java -jar app.jar
