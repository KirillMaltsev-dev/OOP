package ru.nsu.maltsev.task_2_4_1.checker.check;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

public class TestReportParser {
    public TestSummary parse(Path taskDirectory) {
        TestSummary summary = new TestSummary();
        Path reportsDirectory = taskDirectory.resolve("build").resolve("test-results").resolve("test");

        if (!Files.exists(reportsDirectory)) {
            return summary;
        }

        try (Stream<Path> files = Files.list(reportsDirectory)) {
            files.filter(path -> path.getFileName().toString().endsWith(".xml"))
                    .forEach(path -> parseFile(path, summary));
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot parse test reports: " + reportsDirectory, exception);
        }

        return summary;
    }

    private void parseFile(Path path, TestSummary summary) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element root = documentBuilder.parse(path.toFile()).getDocumentElement();

            int tests = intAttribute(root, "tests");
            int failures = intAttribute(root, "failures");
            int errors = intAttribute(root, "errors");
            int skipped = intAttribute(root, "skipped");

            summary.add(tests, failures + errors, skipped);
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot parse test report: " + path, exception);
        }
    }

    private int intAttribute(Element element, String name) {
        String value = element.getAttribute(name);
        if (value == null || value.isBlank()) {
            return 0;
        }

        return Integer.parseInt(value);
    }
}