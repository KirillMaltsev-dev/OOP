package ru.nsu.maltsev.task_1_4_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Главный класс электронной зачетной книжки студента
 * Содержит все функции расчета согласно требованиям задания
 */
public class StudentGradeBook {
    private String fullName;
    private String specialization;
    private TuitionForm tuitionForm;
    private int admissionYear;
    private List<Grade> grades;
    private int currentSemester;
    
    // Пороговые значения для различных расчетов
    private static final int EXCELLENT_GRADE = 5;
    private static final int GOOD_GRADE = 4;
    private static final int SATISFACTORY_GRADE = 3;
    private static final int POOR_GRADE = 2;
    private static final double RED_DIPLOMA_THRESHOLD = 0.75;  // 75%
    
    /**
     * Конструктор электронной зачетной книжки
     */
    public StudentGradeBook(String fullName, String specialization, 
                           TuitionForm tuitionForm, int admissionYear) {
        this.fullName = fullName;
        this.specialization = specialization;
        this.tuitionForm = tuitionForm;
        this.admissionYear = admissionYear;
        this.grades = new ArrayList<>();
        this.currentSemester = 1;
    }
    
    // ========== GETTERS И SETTERS ==========
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public TuitionForm getTuitionForm() {
        return tuitionForm;
    }
    
    public void setTuitionForm(TuitionForm tuitionForm) {
        this.tuitionForm = tuitionForm;
    }
    
    public int getAdmissionYear() {
        return admissionYear;
    }
    
    public int getCurrentSemester() {
        return currentSemester;
    }
    
    public void setCurrentSemester(int currentSemester) {
        if (currentSemester < 1 || currentSemester > 8) {
            throw new IllegalArgumentException("Семестр должен быть от 1 до 8");
        }
        this.currentSemester = currentSemester;
    }
    
    /**
     * Добавляет оценку в зачетную книжку
     */
    public void addGrade(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Оценка не может быть null");
        }
        grades.add(grade);
    }
    
    /**
     * Удаляет оценку из зачетной книжки
     */
    public void removeGrade(Grade grade) {
        grades.remove(grade);
    }
    
    /**
     * Возвращает все оценки студента
     */
    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades);
    }
    
    /**
     * Возвращает оценки за конкретный семестр
     */
    public List<Grade> getGradesBySemester(int semester) {
        return grades.stream()
                .filter(g -> g.getSemester() == semester)
                .collect(Collectors.toList());
    }
    
    // ========== ФУНКЦИЯ 1: СРЕДНИЙ БАЛЛ ==========
    
    /**
     * Вычисляет средний балл за все время обучения
     * @return средний балл (формат: 4.25)
     */
    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        
        double sum = grades.stream()
                .mapToInt(Grade::getValue)
                .sum();
        
        return Math.round((sum / grades.size()) * 100.0) / 100.0;
    }
    
    /**
     * Вычисляет средний балл за конкретный семестр
     */
    public double calculateGPAForSemester(int semester) {
        List<Grade> semesterGrades = getGradesBySemester(semester);
        
        if (semesterGrades.isEmpty()) {
            return 0.0;
        }
        
        double sum = semesterGrades.stream()
                .mapToInt(Grade::getValue)
                .sum();
        
        return Math.round((sum / semesterGrades.size()) * 100.0) / 100.0;
    }
    
    /**
     * Вычисляет средний балл по итоговым оценкам (экзамены + диф. зачеты)
     */
    public double calculateGPAForFinalGrades() {
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isFinal)
                .collect(Collectors.toList());
        
        if (finalGrades.isEmpty()) {
            return 0.0;
        }
        
        double sum = finalGrades.stream()
                .mapToInt(Grade::getValue)
                .sum();
        
        return Math.round((sum / finalGrades.size()) * 100.0) / 100.0;
    }
    
    // ========== ФУНКЦИЯ 2: ПЕРЕВОД НА БЮДЖЕТ ==========
    
    /**
     * Проверяет возможность перевода со студента с платной на бюджетную форму обучения
     * 
     * Требование: отсутствие оценок "удовлетворительно" (3) за последние 
     * две экзаменационные сессии (за экзамены; в дифференцированных зачетах допустимы оценки 3)
     * 
     * @return true если студент может перевестись на бюджет, false иначе
     */
    public boolean canTransferToBudget() {
        // Если уже на бюджете, перевод не требуется
        if (tuitionForm == TuitionForm.BUDGET) {
            return false;
        }
        
        // Если нет оценок, нельзя проверить
        if (grades.isEmpty()) {
            return false;
        }
        
        // Получаем только экзамены (не дифференцированные зачеты!)
        List<Grade> exams = grades.stream()
                .filter(g -> g.getAssessmentType() == AssessmentType.EXAM)
                .collect(Collectors.toList());
        
        if (exams.isEmpty()) {
            return false;
        }
        
        // Определяем номера последних двух семестров с экзаменами
        Set<Integer> semesters = exams.stream()
                .map(Grade::getSemester)
                .collect(Collectors.toSet());
        
        List<Integer> sortedSemesters = semesters.stream()
                .sorted(Collections.reverseOrder())
                .limit(2)
                .collect(Collectors.toList());
        
        // Если менее 2 семестров с экзаменами, недостаточно информации
        if (sortedSemesters.size() < 2) {
            return false;
        }
        
        // Проверяем, нет ли троек в экзаменах за последние 2 семестра
        boolean hasThreesInLastTwoSemesters = exams.stream()
                .filter(g -> sortedSemesters.contains(g.getSemester()))
                .anyMatch(g -> g.getValue() == SATISFACTORY_GRADE);
        
        return !hasThreesInLastTwoSemesters;
    }
    
    /**
     * Возвращает подробную информацию о возможности перевода
     */
    public String getTransferToBudgetInfo() {
        if (tuitionForm == TuitionForm.BUDGET) {
            return "Студент уже обучается на бюджетной форме";
        }
        
        List<Grade> exams = grades.stream()
                .filter(g -> g.getAssessmentType() == AssessmentType.EXAM)
                .collect(Collectors.toList());
        
        if (exams.isEmpty()) {
            return "Нет сданных экзаменов для проверки";
        }
        
        Set<Integer> semesters = exams.stream()
                .map(Grade::getSemester)
                .collect(Collectors.toSet());
        
        List<Integer> sortedSemesters = semesters.stream()
                .sorted(Collections.reverseOrder())
                .limit(2)
                .collect(Collectors.toList());
        
        if (sortedSemesters.size() < 2) {
            return "Недостаточно семестров с экзаменами";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("Проверка последних двух семестров (только экзамены):\n");
        
        for (Integer semester : sortedSemesters) {
            List<Grade> semesterExams = exams.stream()
                    .filter(g -> g.getSemester() == semester)
                    .collect(Collectors.toList());
            
            long threeCount = semesterExams.stream()
                    .filter(g -> g.getValue() == SATISFACTORY_GRADE)
                    .count();
            
            info.append(String.format("  Семестр %d: %d оценок 3 из %d экзаменов\n",
                    semester, threeCount, semesterExams.size()));
        }
        
        if (canTransferToBudget()) {
            info.append("\n✓ РЕЗУЛЬТАТ: Студент может перевестись на бюджет");
        } else {
            info.append("\n✗ РЕЗУЛЬТАТ: Требования не выполнены");
        }
        
        return info.toString();
    }
    
    // ========== ФУНКЦИЯ 3: КРАСНЫЙ ДИПЛОМ ==========
    
    /**
     * Проверяет возможность получения "красного" диплома с отличием
     * 
     * Требования:
     * 1. 75% оценок в приложении к диплому (последняя оценка) – "отлично" (5)
     * 2. Отсутствие итоговых оценок "удовлетворительно" (3) 
     *    (как по дифференцированным зачетам, так и по экзаменам)
     * 3. Квалификационная работа на "отлично"
     * 
     * @return true если все условия выполнены, false иначе
     */
    public boolean canGetRedDiploma() {
        // Получаем итоговые оценки (экзамены + дифф. зачеты)
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isFinal)
                .collect(Collectors.toList());
        
        // Условие 3: Ищем квалификационную работу (ВКР)
        Optional<Grade> qualificationWork = grades.stream()
                .filter(g -> g.getAssessmentType() == AssessmentType.QUALIFICATION_WORK)
                .findFirst();
        
        // Если ВКР не защищена, можем прогнозировать только если у нас хорошие оценки
        if (qualificationWork.isPresent() && 
            qualificationWork.get().getValue() != EXCELLENT_GRADE) {
            return false;
        }
        
        // Условие 2: Проверяем отсутствие троек в итоговых оценках
        boolean hasThreesInFinalGrades = finalGrades.stream()
                .anyMatch(g -> g.getValue() == SATISFACTORY_GRADE);
        
        if (hasThreesInFinalGrades) {
            return false;
        }
        
        // Условие 1: 75% оценок должны быть "отличными"
        if (finalGrades.isEmpty()) {
            return false;  // Невозможно оценить без достаточных итоговых оценок
        }
        
        long excellentCount = finalGrades.stream()
                .filter(g -> g.getValue() == EXCELLENT_GRADE)
                .count();
        
        double excellentPercentage = (double) excellentCount / finalGrades.size();
        
        return excellentPercentage >= RED_DIPLOMA_THRESHOLD;
    }
    
    /**
     * Вычисляет процент отличных оценок
     */
    public double getExcellentGradePercentage() {
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isFinal)
                .collect(Collectors.toList());
        
        if (finalGrades.isEmpty()) {
            return 0.0;
        }
        
        long excellentCount = finalGrades.stream()
                .filter(g -> g.getValue() == EXCELLENT_GRADE)
                .count();
        
        return Math.round(((double) excellentCount / finalGrades.size()) * 10000.0) / 100.0;
    }
    
    /**
     * Возвращает подробную информацию о шансах на красный диплом
     */
    public String getRedDiplomaInfo() {
        StringBuilder info = new StringBuilder();
        
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isFinal)
                .collect(Collectors.toList());
        
        info.append("=== ПРОВЕРКА УСЛОВИЙ КРАСНОГО ДИПЛОМА ===\n\n");
        
        // Условие 1: Процент пятерок
        long excellentCount = finalGrades.stream()
                .filter(g -> g.getValue() == EXCELLENT_GRADE)
                .count();
        double excellentPercentage = finalGrades.isEmpty() ? 0 : 
                (double) excellentCount / finalGrades.size() * 100;
        
        info.append(String.format("1. Процент оценок 'отлично': %.1f%% из %d итоговых оценок\n",
                excellentPercentage, finalGrades.size()));
        info.append(String.format("   Требуется: >= 75.0%%\n"));
        info.append(String.format("   Статус: %s\n\n",
                excellentPercentage >= 75 ? "✓ ВЫПОЛНЕНО" : "✗ НЕ ВЫПОЛНЕНО"));
        
        // Условие 2: Отсутствие троек
        long threeCount = finalGrades.stream()
                .filter(g -> g.getValue() == SATISFACTORY_GRADE)
                .count();
        
        info.append(String.format("2. Отсутствие оценок 'удовлетворительно': найдено %d оценок 3\n",
                threeCount));
        info.append(String.format("   Требуется: 0 оценок\n"));
        info.append(String.format("   Статус: %s\n\n",
                threeCount == 0 ? "✓ ВЫПОЛНЕНО" : "✗ НЕ ВЫПОЛНЕНО"));
        
        // Условие 3: Квалификационная работа
        Optional<Grade> qualificationWork = grades.stream()
                .filter(g -> g.getAssessmentType() == AssessmentType.QUALIFICATION_WORK)
                .findFirst();
        
        if (qualificationWork.isPresent()) {
            int vkrGrade = qualificationWork.get().getValue();
            info.append(String.format("3. Квалификационная работа: оценка %d\n", vkrGrade));
            info.append(String.format("   Требуется: 5\n"));
            info.append(String.format("   Статус: %s\n\n",
                    vkrGrade == EXCELLENT_GRADE ? "✓ ВЫПОЛНЕНО" : "✗ НЕ ВЫПОЛНЕНО"));
        } else {
            info.append("3. Квалификационная работа: еще не защищена\n");
            info.append("   Требуется: оценка 5\n");
            info.append("   Статус: ⚠ ЕЩЕ НЕ ЗАЩИЩЕНА\n\n");
        }
        
        // Итоговый результат
        info.append("=== ИТОГОВЫЙ РЕЗУЛЬТАТ ===\n");
        if (canGetRedDiploma()) {
            info.append("✓ ПУТЬ К КРАСНОМУ ДИПЛОМУ ОТКРЫТ!");
        } else {
            info.append("✗ ТРЕБОВАНИЯ НЕ ВЫПОЛНЕНЫ");
        }
        
        return info.toString();
    }
    
    // ========== ФУНКЦИЯ 4: ПОВЫШЕННАЯ СТИПЕНДИЯ ==========
    
    /**
     * Проверяет возможность получения повышенной стипендии в текущем семестре
     * 
     * Требование: все оценки в текущем семестре должны быть "отличными" (5)
     * 
     * @return true если все оценки в текущем семестре - пятерки, false иначе
     */
    public boolean canGetPerformanceScholarship() {
        List<Grade> currentSemesterGrades = getGradesBySemester(currentSemester);
        
        // Должны быть хотя бы какие-то оценки
        if (currentSemesterGrades.isEmpty()) {
            return false;
        }
        
        // Все оценки должны быть пятерками
        return currentSemesterGrades.stream()
                .allMatch(g -> g.getValue() == EXCELLENT_GRADE);
    }
    
    /**
     * Возвращает подробную информацию о возможности получения повышенной стипендии
     */
    public String getPerformanceScholarshipInfo() {
        List<Grade> currentSemesterGrades = getGradesBySemester(currentSemester);
        
        if (currentSemesterGrades.isEmpty()) {
            return String.format("В семестре %d нет оценок", currentSemester);
        }
        
        long fiveCount = currentSemesterGrades.stream()
                .filter(g -> g.getValue() == EXCELLENT_GRADE)
                .count();
        
        long fourCount = currentSemesterGrades.stream()
                .filter(g -> g.getValue() == GOOD_GRADE)
                .count();
        
        long threeCount = currentSemesterGrades.stream()
                .filter(g -> g.getValue() == SATISFACTORY_GRADE)
                .count();
        
        StringBuilder info = new StringBuilder();
        info.append(String.format("Семестр %d - Статистика оценок:\n", currentSemester));
        info.append(String.format("  Оценок 5: %d\n", fiveCount));
        info.append(String.format("  Оценок 4: %d\n", fourCount));
        info.append(String.format("  Оценок 3: %d\n", threeCount));
        info.append(String.format("  Всего: %d\n\n", currentSemesterGrades.size()));
        
        if (canGetPerformanceScholarship()) {
            info.append("✓ Студент имеет право на повышенную стипендию!");
        } else {
            info.append("✗ Требуется только оценки 5 в семестре");
        }
        
        return info.toString();
    }
    
    // ========== УТИЛИТЫ И ВЫВОД ==========
    
    /**
     * Возвращает статистику по оценкам
     */
    public String getStatistics() {
        if (grades.isEmpty()) {
            return "Оценок еще нет";
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== СТАТИСТИКА ПО ОЦЕНКАМ ===\n\n");
        
        long fiveCount = grades.stream().filter(g -> g.getValue() == 5).count();
        long fourCount = grades.stream().filter(g -> g.getValue() == 4).count();
        long threeCount = grades.stream().filter(g -> g.getValue() == 3).count();
        long twoCount = grades.stream().filter(g -> g.getValue() == 2).count();
        
        stats.append(String.format("Оценок 5 (отлично): %d (%.1f%%)\n",
                fiveCount, (double) fiveCount / grades.size() * 100));
        stats.append(String.format("Оценок 4 (хорошо): %d (%.1f%%)\n",
                fourCount, (double) fourCount / grades.size() * 100));
        stats.append(String.format("Оценок 3 (удовлетворительно): %d (%.1f%%)\n",
                threeCount, (double) threeCount / grades.size() * 100));
        stats.append(String.format("Оценок 2 (неудовлетворительно): %d (%.1f%%)\n",
                twoCount, (double) twoCount / grades.size() * 100));
        stats.append(String.format("\nВсего оценок: %d\n", grades.size()));
        stats.append(String.format("Средний балл: %.2f\n", calculateGPA()));
        
        return stats.toString();
    }
    
    /**
     * Выводит информацию о студенте
     */
    @Override
    public String toString() {
        return String.format("=== ЗАЧЕТНАЯ КНИЖКА ===\n" +
                "ФИО: %s\n" +
                "Специальность: %s\n" +
                "Форма обучения: %s\n" +
                "Год поступления: %d\n" +
                "Текущий семестр: %d",
                fullName, specialization, tuitionForm, admissionYear, currentSemester);
    }
}
