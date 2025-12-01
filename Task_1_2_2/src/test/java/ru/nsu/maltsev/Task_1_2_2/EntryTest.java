package ru.nsu.maltsev.task_1_2_2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EntryTest {

    @Test
    void testEntryCreationAndGetters() {
        Entry<String, Integer> entry = new Entry<>("key", 100);

        assertEquals("key", entry.getKey());
        assertEquals(100, entry.getValue());
        assertNull(entry.getNext());
    }

    @Test
    void testSetters() {
        Entry<String, Integer> entry = new Entry<>("key", 100);

        entry.setValue(200);
        assertEquals(200, entry.getValue());

        Entry<String, Integer> nextEntry = new Entry<>("next", 300);
        entry.setNext(nextEntry);

        assertNotNull(entry.getNext());
        assertEquals(nextEntry, entry.getNext());
        assertEquals("next", entry.getNext().getKey());
    }

    @Test
    void testEqualsAndHashCode() {
        Entry<String, Integer> entry1 = new Entry<>("A", 1);
        Entry<String, Integer> entry2 = new Entry<>("A", 1);
        Entry<String, Integer> entry3 = new Entry<>("B", 2);

        assertEquals(entry1, entry1);
        assertEquals(entry1, entry2);
        assertEquals(entry1.hashCode(), entry2.hashCode());

        assertNotEquals(entry1, entry3);
        assertNotEquals(entry1, null);
        assertNotEquals(entry1, new Object());
    }

    @Test
    void testToString() {
        Entry<String, String> entry = new Entry<>("Hello", "World");
        assertEquals("Hello=World", entry.toString());
    }
}
