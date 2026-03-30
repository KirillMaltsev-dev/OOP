package ru.nsu.maltsev.task_2_2_1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonConfigParser {
    private JsonConfigParser() {
    }

    public static PizzeriaConfig parse(String filePath) throws IOException {
        String json = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        int warehouseCapacity = extractInt(json, "warehouseCapacity");
        int workDurationMs = extractInt(json, "workDurationMs");
        int orderIntervalMs = extractInt(json, "orderIntervalMs");
        int deliveryTimeMs = extractInt(json, "deliveryTimeMs");
        int ordersCount = extractInt(json, "ordersCount");
        List<Integer> bakers = extractIntArray(json, "bakers");
        List<Integer> couriers = extractIntArray(json, "couriers");
        return new PizzeriaConfig(
                warehouseCapacity,
                workDurationMs,
                orderIntervalMs,
                deliveryTimeMs,
                ordersCount,
                bakers,
                couriers
        );
    }

    private static int extractInt(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing integer field: " + key);
        }
        return Integer.parseInt(matcher.group(1));
    }

    private static List<Integer> extractIntArray(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing array field: " + key);
        }
        String body = matcher.group(1).trim();
        List<Integer> result = new ArrayList<>();
        if (body.isEmpty()) {
            return result;
        }
        String[] parts = body.split(",");
        for (String part : parts) {
            result.add(Integer.parseInt(part.trim()));
        }
        return result;
    }
}