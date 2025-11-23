package ru.nsu.maltsev.Task_1_2_2;

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HashTable<K, V> implements Iterable<Entry<K, V>> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private int size;
    private int modCount;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode() % table.length);
    }

    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.setNext(table[index]);
        table[index] = newEntry;
        size++;
        modCount++;
    }

    public void update(K key, V value) {
        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                if (prev == null) {
                    table[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                modCount++;
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[oldTable.length * 2];
        size = 0;
        //modCount не сбрасываем, так как resize - это тоже модификация
        //но внутри put он и так увеличится, так что можно оставить как есть

        for (Entry<K, V> head : oldTable) {
            while (head != null) {
                put(head.getKey(), head.getValue());
                head = head.getNext();
            }
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new TableIterator();
    }

    @Override
    @SuppressWarnings("unchecked") //Подавляем предупреждение о непроверяемом приведении типов
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashTable<K, V> that = (HashTable<K, V>) o;

        if (this.size != that.size) return false;

        for (Entry<K, V> entry : this) {
            K key = entry.getKey();
            V value = entry.getValue();
            Object thatValue = that.get(key);

            if (!Objects.equals(value, thatValue)) {
                return false;
            }
            if (thatValue == null && !that.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator<Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private class TableIterator implements Iterator<Entry<K, V>> {
        private int currentBucketIndex;
        private Entry<K, V> currentEntry;
        private Entry<K, V> lastReturned;
        private int expectedModCount;
        private int itemsIterated;

        public TableIterator() {
            this.expectedModCount = modCount;
            this.currentBucketIndex = 0;
            this.itemsIterated = 0;
            this.currentEntry = null;
            prepareNext();
        }

        private void prepareNext() {
            if (currentEntry != null && currentEntry.getNext() != null) {
                currentEntry = currentEntry.getNext();
                return;
            }
            if (currentEntry != null) {
                currentBucketIndex++;
            }

            while (currentBucketIndex < table.length) {
                if (table[currentBucketIndex] != null) {
                    currentEntry = table[currentBucketIndex];
                    return;
                }
                currentBucketIndex++;
            }
            currentEntry = null;
        }

        @Override
        public boolean hasNext() {
            return itemsIterated < size;
        }

        @Override
        public Entry<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturned = currentEntry;
            itemsIterated++;
            prepareNext();
            return lastReturned;
        }
    }
}

