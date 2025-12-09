package ru.nsu.maltsev.task_1_4_1;
/**
 * Перечисление форм обучения
 */
public enum TuitionForm {
    PAID("платная"),
    BUDGET("бюджетная");
    
    private final String description;
    
    TuitionForm(String description) {
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
