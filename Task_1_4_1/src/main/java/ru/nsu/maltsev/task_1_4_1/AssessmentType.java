package ru.nsu.maltsev.task_1_4_1;
/**
 * Перечисление типов оценок
 */
public enum AssessmentType {
    EXAM("Экзамен"),
    DIFFERENTIAL_GRADE("Дифференцированный зачет"),
    CREDIT("Зачет"),
    CONTROL_WORK("Контрольная работа"),
    ASSIGNMENT("Задание"),
    COLLOQUIUM("Коллоквиум"),
    PRACTICE_REPORT("Защита отчета по практике"),
    QUALIFICATION_WORK("Защита ВКР");
    
    private final String description;
    
    AssessmentType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
