package ru.nsu.maltsev.task_1_4_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GradeTest {

    @Test
    void testValidGradeCreation() {
        Grade grade = new Grade("Math", AssessmentType.EXAM, 5, 1);

        assertEquals("Math", grade.getDiscipline());
        assertEquals(AssessmentType.EXAM, grade.getAssessmentType());
        assertEquals(5, grade.getValue());
        assertEquals(1, grade.getSemester());
    }

    @Test
    void testInvalidValueThrowsException() {
        // Проверка нижней границы
        Exception lowEx = assertThrows(IllegalArgumentException.class, () ->
                new Grade("Math", AssessmentType.EXAM, 1, 1));
        assertTrue(lowEx.getMessage().contains("от 2 до 5"));

        // Проверка верхней границы
        Exception highEx = assertThrows(IllegalArgumentException.class, () ->
                new Grade("Math", AssessmentType.EXAM, 6, 1));
        assertTrue(highEx.getMessage().contains("от 2 до 5"));
    }

    @Test
    void testInvalidSemesterThrowsException() {
        // Проверка нижней границы семестра
        assertThrows(IllegalArgumentException.class, () ->
                new Grade("Math", AssessmentType.EXAM, 5, 0));

        // Проверка верхней границы семестра
        assertThrows(IllegalArgumentException.class, () ->
                new Grade("Math", AssessmentType.EXAM, 5, 9));
    }

    @Test
    void testIsFinal() {
        Grade exam = new Grade("A", AssessmentType.EXAM, 5, 1);
        Grade diff = new Grade("B", AssessmentType.DIFFERENTIAL_GRADE, 5, 1);
        Grade credit = new Grade("C", AssessmentType.CREDIT, 5, 1);

        assertTrue(exam.isFinal());
        assertTrue(diff.isFinal());
        assertFalse(credit.isFinal());
    }

    @Test
    void testIsExam() {
        Grade exam = new Grade("A", AssessmentType.EXAM, 5, 1);
        Grade diff = new Grade("B", AssessmentType.DIFFERENTIAL_GRADE, 5, 1);

        assertTrue(exam.isExam());
        assertFalse(diff.isExam());
    }

    @Test
    void testToString() {
        Grade grade = new Grade("Math", AssessmentType.EXAM, 5, 1);
        String s = grade.toString();
        assertTrue(s.contains("Math"));
        assertTrue(s.contains("5"));
    }
}
