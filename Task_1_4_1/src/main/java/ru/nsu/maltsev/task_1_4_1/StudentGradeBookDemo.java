package ru.nsu.maltsev.task_1_4_1;
/**
 * Тестовый класс для демонстрации функциональности электронной зачетной книжки
 * Содержит примеры использования всех четырех требуемых функций
 */
public class StudentGradeBookDemo {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   ЭЛЕКТРОННАЯ ЗАЧЕТНАЯ КНИЖКА СТУДЕНТА ФИТ                 ║");
        System.out.println("║   Демонстрация всех функций                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Создание зачетной книжки студента
        StudentGradeBook gradeBook = new StudentGradeBook(
                "Мальцев Кирилл Константинович",
                "Компьютерные науки и системотехника (09.12.25)",
                TuitionForm.PAID,
                2024
        );
        
        System.out.println(gradeBook);
        System.out.println("\n");
        
        // ============ ДОБАВЛЕНИЕ ОЦЕНОК ============
        
        // Семестр 1
        gradeBook.addGrade(new Grade("Математический анализ", AssessmentType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Алгебра", AssessmentType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Программирование на Java", AssessmentType.DIFFERENTIAL_GRADE, 5, 1));
        
        // Семестр 2
        gradeBook.addGrade(new Grade("Теория вероятностей", AssessmentType.EXAM, 5, 2));
        gradeBook.addGrade(new Grade("Дискретная математика", AssessmentType.EXAM, 4, 2));
        gradeBook.addGrade(new Grade("Информатика", AssessmentType.DIFFERENTIAL_GRADE, 5, 2));
        
        // Семестр 3
        gradeBook.addGrade(new Grade("Базы данных", AssessmentType.EXAM, 5, 3));
        gradeBook.addGrade(new Grade("Архитектура ЭВМ", AssessmentType.EXAM, 5, 3));
        gradeBook.addGrade(new Grade("Веб-приложения", AssessmentType.DIFFERENTIAL_GRADE, 5, 3));
        
        // Семестр 4 (текущий)
        gradeBook.setCurrentSemester(4);
        gradeBook.addGrade(new Grade("ОС и системное программирование", AssessmentType.EXAM, 5, 4));
        gradeBook.addGrade(new Grade("Компиляторы", AssessmentType.EXAM, 5, 4));
        gradeBook.addGrade(new Grade("Сетевые технологии", AssessmentType.DIFFERENTIAL_GRADE, 5, 4));
        gradeBook.addGrade(new Grade("Практика", AssessmentType.PRACTICE_REPORT, 5, 4));
        
        // Квалификационная работа
        gradeBook.addGrade(new Grade("Защита выпускной квалификационной работы", 
                AssessmentType.QUALIFICATION_WORK, 5, 8));
        
        // ============ ФУНКЦИЯ 1: СРЕДНИЙ БАЛЛ ============
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ 1. ФУНКЦИЯ: ВЫЧИСЛЕНИЕ ТЕКУЩЕГО СРЕДНЕГО БАЛЛА             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.printf("Средний балл ЗА ВСЕ ВРЕМЯ: %.2f\n", gradeBook.calculateGPA());
        System.out.printf("Средний балл ЗА СЕМЕСТР 1: %.2f\n", gradeBook.calculateGPAForSemester(1));
        System.out.printf("Средний балл ЗА СЕМЕСТР 2: %.2f\n", gradeBook.calculateGPAForSemester(2));
        System.out.printf("Средний балл ЗА СЕМЕСТР 3: %.2f\n", gradeBook.calculateGPAForSemester(3));
        System.out.printf("Средний балл ЗА СЕМЕСТР 4: %.2f\n", gradeBook.calculateGPAForSemester(4));
        System.out.printf("\nСредний балл ПО ИТОГОВЫМ ОЦЕНКАМ (экзамены + диф. зачеты): %.2f\n\n",
                gradeBook.calculateGPAForFinalGrades());
        
        // ============ ФУНКЦИЯ 2: ПЕРЕВОД НА БЮДЖЕТ ============
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ 2. ФУНКЦИЯ: ПРОВЕРКА ПЕРЕВОДА НА БЮДЖЕТНУЮ ФОРМУ           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println(gradeBook.getTransferToBudgetInfo());
        System.out.println("\n");
        
        // ============ ФУНКЦИЯ 3: КРАСНЫЙ ДИПЛОМ ============
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ 3. ФУНКЦИЯ: ПРОВЕРКА ВОЗМОЖНОСТИ КРАСНОГО ДИПЛОМА          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println(gradeBook.getRedDiplomaInfo());
        System.out.println("\n");
        
        // ============ ФУНКЦИЯ 4: ПОВЫШЕННАЯ СТИПЕНДИЯ ============
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ 4. ФУНКЦИЯ: ПРОВЕРКА ПОВЫШЕННОЙ СТИПЕНДИИ                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println(gradeBook.getPerformanceScholarshipInfo());
        System.out.println("\n");
        
        // ============ СТАТИСТИКА ============
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ ОБЩАЯ СТАТИСТИКА                                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println(gradeBook.getStatistics());
        System.out.println("\n");
        
        // ============ СПИСОК ВСЕХ ОЦЕНОК ============
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ ИСТОРИЯ ВСЕХ ОЦЕНОК                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        int counter = 1;
        for (Grade grade : gradeBook.getAllGrades()) {
            System.out.printf("%2d. %s\n", counter++, grade);
        }
        
        // ============ ДЕМОНСТРАЦИЯ СЦЕНАРИЯ С НЕУДАЧЕЙ ============
        System.out.println("\n\n ╔════════════════════════════════════════════════════════════════╗");
        System.out.println(" ║ СЦЕНАРИЙ 2: СТУДЕНТ НА ПЛАТНОЙ ФОРМЕ БЕЗ ПРАВА ПЕРЕВОДА        ║");
        System.out.println(" ╚════════════════════════════════════════════════════════════════╝\n");
        
        StudentGradeBook gradeBook2 = new StudentGradeBook(
                "Петр Петрович Петров",
                "Прикладная информатика (09.03.03)",
                TuitionForm.PAID,
                2021
        );
        
        // Добавляем оценки с тройками в экзаменах
        gradeBook2.addGrade(new Grade("Математический анализ", AssessmentType.EXAM, 3, 1));
        gradeBook2.addGrade(new Grade("Программирование", AssessmentType.DIFFERENTIAL_GRADE, 4, 1));
        gradeBook2.addGrade(new Grade("Теория вероятностей", AssessmentType.EXAM, 3, 2));
        
        System.out.println(gradeBook2);
        System.out.println("\n");
        System.out.println(gradeBook2.getTransferToBudgetInfo());
        System.out.println("\n");
        System.out.println(gradeBook2.getRedDiplomaInfo());
    }
}
