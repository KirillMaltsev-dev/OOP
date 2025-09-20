#!/bin/bash

rm -rf build docs app.jar
mkdir -p build docs

# Компиляция исходных файлов
javac -d build src/main/java/ru/nsu/maltsev/Task_1_1_1/Heapsort.java src/main/java/ru/nsu/maltsev/Task_1_1_1/Main.java

# Генерация Javadoc
javadoc -d docs src/main/java/ru/nsu/maltsev/Task_1_1_1/Heapsort.java src/main/java/ru/nsu/maltsev/Task_1_1_1/Main.java

# Создание JAR-файла
jar cfe app.jar ru.nsu.maltsev.Main -C build .

# Запуск приложения из JAR-файла
java -jar app.jar
