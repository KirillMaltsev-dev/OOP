package ru.nsu.maltsev.task_2_1_2;

import java.util.zip.CRC32;

public final class Protocol {
    public static final String CHECK_TASK = "CHECK_TASK";
    public static final String RESULT = "RESULT";
    public static final String ERROR = "ERROR";

    private Protocol() {
    }

    public static long checksum(int[] numbers) {
        CRC32 crc32 = new CRC32();
        for (int value : numbers) {
            crc32.update((value >>> 24) & 0xFF);
            crc32.update((value >>> 16) & 0xFF);
            crc32.update((value >>> 8) & 0xFF);
            crc32.update(value & 0xFF);
        }
        return crc32.getValue();
    }
}