package ru.nsu.maltsev.task_2_4_1.checker.report;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.maltsev.task_2_4_1.checker.check.TaskCheckResult;
import ru.nsu.maltsev.task_2_4_1.checker.grading.GradeCalculator;
import ru.nsu.maltsev.task_2_4_1.checker.model.CourseConfig;
import ru.nsu.maltsev.task_2_4_1.checker.model.StudentConfig;

public class HtmlReportGenerator {
    private final CourseConfig config;
    private final GradeCalculator gradeCalculator;

    public HtmlReportGenerator(CourseConfig config) {
        this.config = config;
        this.gradeCalculator = new GradeCalculator(config.getSettings());
    }

    public String generate(List<TaskCheckResult> results) {
        StringBuilder html = new StringBuilder();

        html.append("<!doctype html>\n");
        html.append("<html lang=\"ru\">\n");
        html.append("<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<title>OOP Checker Report</title>\n");
        html.append("<style>\n");
        html.append("body{font-family:Arial,sans-serif;margin:24px;}\n");
        html.append("table{border-collapse:collapse;width:100%;margin-bottom:24px;}\n");
        html.append("th,td{border:1px solid #ccc;padding:8px;text-align:left;vertical-align:top;}\n");
        html.append("th{background:#f2f2f2;}\n");
        html.append(".ok{color:green;font-weight:bold;}\n");
        html.append(".fail{color:#b00020;font-weight:bold;}\n");
        html.append("</style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        html.append("<h1>Отчёт автоматической проверки ООП</h1>\n");
        appendSummary(html, results);
        appendDetailedTable(html, results);

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    private void appendSummary(StringBuilder html, List<TaskCheckResult> results) {
        Map<String, Double> pointsByStudent = new LinkedHashMap<>();
        Map<String, StudentConfig> studentsByGithub = new LinkedHashMap<>();

        for (TaskCheckResult result : results) {
            String github = result.getStudent().getGithub();
            pointsByStudent.merge(github, result.getPoints(), Double::sum);
            studentsByGithub.put(github, result.getStudent());
        }

        html.append("<h2>Итоги по студентам</h2>\n");
        html.append("<table>\n");
        html.append("<tr><th>Студент</th><th>GitHub</th><th>Баллы</th><th>Оценка</th></tr>\n");

        for (Map.Entry<String, Double> entry : pointsByStudent.entrySet()) {
            StudentConfig student = studentsByGithub.get(entry.getKey());
            double points = entry.getValue();

            html.append("<tr>");
            html.append("<td>").append(escape(student.getFullName())).append("</td>");
            html.append("<td>").append(escape(student.getGithub())).append("</td>");
            html.append("<td>").append(points).append("</td>");
            html.append("<td>").append(escape(gradeCalculator.calculate(points))).append("</td>");
            html.append("</tr>\n");
        }

        html.append("</table>\n");
    }

    private void appendDetailedTable(StringBuilder html, List<TaskCheckResult> results) {
        html.append("<h2>Детальная проверка</h2>\n");
        html.append("<table>\n");
        html.append("<tr>");
        html.append("<th>Студент</th>");
        html.append("<th>Задача</th>");
        html.append("<th>Компиляция</th>");
        html.append("<th>Javadoc</th>");
        html.append("<th>Style</th>");
        html.append("<th>Тесты</th>");
        html.append("<th>Баллы</th>");
        html.append("<th>Ошибка</th>");
        html.append("</tr>\n");

        for (TaskCheckResult result : results) {
            html.append("<tr>");
            html.append("<td>").append(escape(result.getStudent().getFullName())).append("</td>");
            html.append("<td>").append(escape(result.getTask().getId())).append("</td>");
            html.append("<td>").append(status(result.isCompilationPassed())).append("</td>");
            html.append("<td>").append(status(result.isJavadocPassed())).append("</td>");
            html.append("<td>").append(status(result.isStylePassed())).append("</td>");
            html.append("<td>").append(testInfo(result)).append("</td>");
            html.append("<td>").append(result.getPoints()).append("</td>");
            html.append("<td><pre>").append(escape(result.getErrorMessage())).append("</pre></td>");
            html.append("</tr>\n");
        }

        html.append("</table>\n");
    }

    private String testInfo(TaskCheckResult result) {
        return status(result.isTestsPassed())
                + "<br>passed: " + result.getTestSummary().getPassed()
                + "<br>failed: " + result.getTestSummary().getFailed()
                + "<br>skipped: " + result.getTestSummary().getSkipped();
    }

    private String status(boolean status) {
        if (status) {
            return "<span class=\"ok\">OK</span>";
        }

        return "<span class=\"fail\">FAIL</span>";
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}