package ru.nsu.maltsev.task_1_4_1;
/**
 * Класс, представляющий одну оценку студента
 */
public class Grade {
    private String discipline;
    private AssessmentType assessmentType;
    private int value;  // от 2 до 5
    private int semester;  // семестр, в котором получена оценка
    
    public Grade(String discipline, AssessmentType assessmentType, int value, int semester) {
        if (value < 2 || value > 5) {
            throw new IllegalArgumentException("Оценка должна быть от 2 до 5");
        }
        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Семестр должен быть от 1 до 8");
        }
        
        this.discipline = discipline;
        this.assessmentType = assessmentType;
        this.value = value;
        this.semester = semester;
    }
    
    // Getters
    public String getDiscipline() {
        return discipline;
    }
    
    public AssessmentType getAssessmentType() {
        return assessmentType;
    }
    
    public int getValue() {
        return value;
    }
    
    public int getSemester() {
        return semester;
    }
    
    /**
     * Проверяет, является ли это оценка итоговой (экзамен или дифференцированный зачет)
     */
    public boolean isFinal() {
        return assessmentType == AssessmentType.EXAM || 
               assessmentType == AssessmentType.DIFFERENTIAL_GRADE;
    }
    
    /**
     * Проверяет, является ли это экзамен
     */
    public boolean isExam() {
        return assessmentType == AssessmentType.EXAM;
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | Оценка: %d | Семестр: %d",
                discipline, assessmentType, value, semester);
    }
}
