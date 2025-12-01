package ru.nsu.maltsev.task_1_2_2;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));

        for (Entry<String, Number> entry : hashTable) {
        }
    }
}
