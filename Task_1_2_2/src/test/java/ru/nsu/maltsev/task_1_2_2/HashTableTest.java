package ru.nsu.maltsev.task_1_2_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HashTableTest {

    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    void testPutAndGet() {
        table.put("one", 1);
        table.put("two", 2);

        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
        assertNull(table.get("three"));
    }

    @Test
    void testUpdate() {
        table.put("key", 10);
        table.update("key", 20);
        assertEquals(20, table.get("key"));
        table.update("missing", 50);
        assertNull(table.get("missing"));
    }

    @Test
    void testRemove() {
        table.put("A", 1);
        table.put("B", 2);

        Integer removed = table.remove("A");
        assertEquals(1, removed);
        assertNull(table.get("A"));
        assertEquals(2, table.get("B"));

        assertNull(table.remove("Z"));
    }

    @Test
    void testContainsKey() {
        table.put("test", 100);
        assertTrue(table.containsKey("test"));
        assertFalse(table.containsKey("fake"));
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> table2 = new HashTable<>();

        table.put("A", 1);
        table.put("B", 2);

        table2.put("B", 2);
        table2.put("A", 1);

        assertEquals(table, table2);

        table2.put("C", 3);
        assertNotEquals(table, table2);
    }

    @Test
    void testCollisions() {
        for (int i = 0; i < 100; i++) {
            table.put("Key" + i, i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get("Key" + i));
        }
    }

    @Test
    void testIterator() {
        table.put("1", 1);
        table.put("2", 2);

        int count = 0;
        for (Entry<String, Integer> entry : table) {
            count++;
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }
        assertEquals(2, count);
    }

    @Test
    void testConcurrentModificationException() {
        table.put("1", 1);
        table.put("2", 2);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (Entry<String, Integer> entry : table) {
                if (entry.getKey().equals("1")) {
                    table.put("3", 3);
                }
            }
        });
    }

    @Test
    void testToString() {
        table.put("A", 1);
        String str = table.toString();
        assertTrue(str.contains("A=1"));
        assertTrue(str.startsWith("{"));
        assertTrue(str.endsWith("}"));
    }
}
