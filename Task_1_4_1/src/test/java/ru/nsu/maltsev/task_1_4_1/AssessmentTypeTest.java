package ru.nsu.maltsev.task_1_4_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AssessmentTypeTest {

    @Test
    void testEnumValues() {
        // Проверяем наличие всех констант (защита от случайного удаления)
        assertEquals(8, AssessmentType.values().length);
        assertNotNull(AssessmentType.valueOf("EXAM"));
        assertNotNull(AssessmentType.valueOf("DIFFERENTIAL_GRADE"));
    }

    @Test
    void testDescriptions() {
        assertEquals("Экзамен", AssessmentType.EXAM.getDescription());
        assertEquals("Зачет", AssessmentType.CREDIT.getDescription());
    }

    @Test
    void testToString() {
        // toString должен возвращать описание
        assertEquals("Экзамен", AssessmentType.EXAM.toString());
    }
}
