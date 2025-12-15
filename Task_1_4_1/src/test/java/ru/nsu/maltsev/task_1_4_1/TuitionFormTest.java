package ru.nsu.maltsev.task_1_4_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TuitionFormTest {

    @Test
    void testEnumValues() {
        assertEquals(2, TuitionForm.values().length);
        assertEquals(TuitionForm.PAID, TuitionForm.valueOf("PAID"));
        assertEquals(TuitionForm.BUDGET, TuitionForm.valueOf("BUDGET"));
    }

    @Test
    void testDescriptions() {
        assertEquals("платная", TuitionForm.PAID.getDescription());
        assertEquals("бюджетная", TuitionForm.BUDGET.getDescription());
    }

    @Test
    void testToString() {
        assertEquals("платная", TuitionForm.PAID.toString());
    }
}
