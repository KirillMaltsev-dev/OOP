package ru.nsu.maltsev.task_1_4_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentGradeBookTest {

    private StudentGradeBook book;

    @BeforeEach
    void setUp() {
        book = new StudentGradeBook("Ivan", "IT", TuitionForm.PAID, 2023);
    }

    // --- Тесты управления данными (Getters/Setters/List) ---

    @Test
    void testBasicGettersAndSetters() {
        assertEquals("Ivan", book.getFullName());
        assertEquals("IT", book.getSpecialization());
        assertEquals(TuitionForm.PAID, book.getTuitionForm());
        assertEquals(2023, book.getAdmissionYear());
        assertEquals(1, book.getCurrentSemester());

        book.setFullName("Petr");
        assertEquals("Petr", book.getFullName());

        book.setTuitionForm(TuitionForm.BUDGET);
        assertEquals(TuitionForm.BUDGET, book.getTuitionForm());
    }

    @Test
    void testSetCurrentSemesterValidation() {
        assertThrows(IllegalArgumentException.class, () -> book.setCurrentSemester(0));
        assertThrows(IllegalArgumentException.class, () -> book.setCurrentSemester(9));
        assertDoesNotThrow(() -> book.setCurrentSemester(8));
    }

    @Test
    void testAddAndRemoveGrade() {
        Grade g = new Grade("Math", AssessmentType.EXAM, 5, 1);

        // Add
        book.addGrade(g);
        assertEquals(1, book.getAllGrades().size());

        // Add null check
        assertThrows(IllegalArgumentException.class, () -> book.addGrade(null));

        // Remove
        book.removeGrade(g);
        assertTrue(book.getAllGrades().isEmpty());
    }

    @Test
    void testGetGradesBySemester() {
        book.addGrade(new Grade("S1", AssessmentType.EXAM, 5, 1));
        book.addGrade(new Grade("S2", AssessmentType.EXAM, 5, 2));

        List<Grade> sem1 = book.getGradesBySemester(1);
        assertEquals(1, sem1.size());
        assertEquals("S1", sem1.get(0).getDiscipline());
    }

    // --- Тесты функции 1: Средний балл ---

    @Test
    void testCalculateGPA() {
        // Пустой
        assertEquals(0.0, book.calculateGPA());

        // Заполненный
        book.addGrade(new Grade("A", AssessmentType.EXAM, 5, 1)); // 5
        book.addGrade(new Grade("B", AssessmentType.EXAM, 4, 1)); // 4
        book.addGrade(new Grade("C", AssessmentType.EXAM, 3, 1)); // 3
        assertEquals(4.0, book.calculateGPA());
    }

    @Test
    void testCalculateGPAForSemester() {
        assertEquals(0.0, book.calculateGPAForSemester(1));

        book.addGrade(new Grade("A", AssessmentType.EXAM, 5, 1));
        book.addGrade(new Grade("B", AssessmentType.EXAM, 3, 2));

        assertEquals(5.0, book.calculateGPAForSemester(1));
    }

    @Test
    void testCalculateGPAForFinalGrades() {
        // Добавляем экзамен (final) и просто задание (not final)
        book.addGrade(new Grade("Final", AssessmentType.EXAM, 5, 1));
        book.addGrade(new Grade("NotFinal", AssessmentType.ASSIGNMENT, 2, 1));

        // Среднее должно считаться только по экзамену (5.0), а не (5+2)/2=3.5
        assertEquals(5.0, book.calculateGPAForFinalGrades());
    }

    // --- Тесты функции 2: Перевод на бюджет ---

    @Test
    void testBudgetTransferFailIfAlreadyBudget() {
        book.setTuitionForm(TuitionForm.BUDGET);
        assertFalse(book.canTransferToBudget());
        assertTrue(book.getTransferToBudgetInfo().contains("уже обучается на бюджетной"));
    }

    @Test
    void testBudgetTransferFailNotEnoughData() {
        // Нет оценок вообще
        assertFalse(book.canTransferToBudget());
        assertTrue(book.getTransferToBudgetInfo().contains("Нет сданных экзаменов"));

        // Только 1 семестр
        book.addGrade(new Grade("Math", AssessmentType.EXAM, 5, 1));
        assertTrue(book.getTransferToBudgetInfo().contains("Недостаточно семестров"));
    }

    @Test
    void testBudgetTransferLogic() {
        // Семестр 1: Экзамен 3 (плохо, но это давно)
        book.addGrade(new Grade("Old", AssessmentType.EXAM, 3, 1));

        // Семестр 2: Экзамены 4, 5 (хорошо)
        book.addGrade(new Grade("S2_1", AssessmentType.EXAM, 4, 2));
        book.addGrade(new Grade("S2_2", AssessmentType.EXAM, 5, 2));

        // Семестр 3: Экзамен 5, ДифЗачет 3 (ДифЗачет 3 допустим!)
        book.addGrade(new Grade("S3_Exam", AssessmentType.EXAM, 5, 3));
        book.addGrade(new Grade("S3_Diff", AssessmentType.DIFFERENTIAL_GRADE, 3, 3));

        // Проверяем последние 2 семестра (2 и 3). Троек в ЭКЗАМЕНАХ нет.
        assertTrue(book.canTransferToBudget());
        assertTrue(book.getTransferToBudgetInfo().contains("РЕЗУЛЬТАТ: Студент может"));

        // Добавляем тройку в экзамен 3-го семестра
        book.addGrade(new Grade("S3_BadExam", AssessmentType.EXAM, 3, 3));
        assertFalse(book.canTransferToBudget());
        assertTrue(book.getTransferToBudgetInfo().contains("РЕЗУЛЬТАТ: Требования не выполнены"));
    }

    // --- Тесты функции 3: Красный диплом ---

    @Test
    void testRedDiplomaBasicFailures() {
        // Нет итоговых оценок
        assertFalse(book.canGetRedDiploma());
        assertEquals(0.0, book.getExcellentGradePercentage());

        // Есть тройка в итоговых
        book.addGrade(new Grade("Bad", AssessmentType.EXAM, 3, 1));
        assertFalse(book.canGetRedDiploma());
        assertTrue(book.getRedDiplomaInfo().contains("найдено 1 оценок 3"));
    }

    @Test
    void testRedDiplomaPercentageLogic() {
        // 4 предмета. Нужно 75% пятерок (3 из 4).

        // Кейс: 50% (2 из 4) -> Fail
        book.addGrade(new Grade("1", AssessmentType.EXAM, 5, 1));
        book.addGrade(new Grade("2", AssessmentType.EXAM, 5, 1));
        book.addGrade(new Grade("3", AssessmentType.EXAM, 4, 1));
        book.addGrade(new Grade("4", AssessmentType.EXAM, 4, 1));
        assertFalse(book.canGetRedDiploma());

        // Кейс: 75% (3 из 4) -> Success
        book.removeGrade(book.getAllGrades().get(3)); // удаляем четверку
        book.addGrade(new Grade("4", AssessmentType.EXAM, 5, 1)); // добавляем пятерку
        assertTrue(book.canGetRedDiploma());
        assertTrue(book.getRedDiplomaInfo().contains("ПУТЬ К КРАСНОМУ ДИПЛОМУ ОТКРЫТ"));
    }

    @Test
    void testRedDiplomaVKR() {
        // Отличные оценки есть
        book.addGrade(new Grade("1", AssessmentType.EXAM, 5, 1));

        // 1. ВКР еще нет -> Прогноз положительный
        assertTrue(book.canGetRedDiploma());
        assertTrue(book.getRedDiplomaInfo().contains("ЕЩЕ НЕ ЗАЩИЩЕНА"));

        // 2. ВКР защищена на 4 -> Fail
        Grade vkr = new Grade("VKR", AssessmentType.QUALIFICATION_WORK, 4, 8);
        book.addGrade(vkr);
        assertFalse(book.canGetRedDiploma());
        assertTrue(book.getRedDiplomaInfo().contains("Квалификационная работа: оценка 4"));

        // 3. ВКР защищена на 5 -> Success
        book.removeGrade(vkr);
        book.addGrade(new Grade("VKR", AssessmentType.QUALIFICATION_WORK, 5, 8));
        assertTrue(book.canGetRedDiploma());
    }

    // --- Тесты функции 4: Повышенная стипендия ---

    @Test
    void testScholarshipEmpty() {
        book.setCurrentSemester(1);
        assertFalse(book.canGetPerformanceScholarship());
        assertTrue(book.getPerformanceScholarshipInfo().contains("нет оценок"));
    }

    @Test
    void testScholarshipLogic() {
        book.setCurrentSemester(2);

        // Оценки в ДРУГОМ семестре не влияют
        book.addGrade(new Grade("Old Bad", AssessmentType.EXAM, 3, 1));

        // В текущем (2) семестре
        book.addGrade(new Grade("Good", AssessmentType.EXAM, 5, 2));
        assertTrue(book.canGetPerformanceScholarship());

        // Добавили четверку
        book.addGrade(new Grade("Average", AssessmentType.EXAM, 4, 2));
        assertFalse(book.canGetPerformanceScholarship());
        assertTrue(book.getPerformanceScholarshipInfo().contains("Требуется только оценки 5"));
    }

    // --- Тесты статистики и toString ---

    @Test
    void testStatisticsAndToString() {
        // Проверяем, что методы не падают и возвращают непустые строки
        assertNotNull(book.toString());
        assertTrue(book.getStatistics().contains("Оценок еще нет"));

        book.addGrade(new Grade("A", AssessmentType.EXAM, 5, 1));
        assertTrue(book.getStatistics().contains("Оценок 5 (отлично): 1"));
    }
}
