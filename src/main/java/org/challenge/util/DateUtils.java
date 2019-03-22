package org.challenge.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final String ISO_8601 = "yyyyMMdd'T'HHmmss.SSS'Z'";

    public static LocalDateTime parse(String updated_at, String iso8601) {
        return LocalDateTime.parse(updated_at, DateTimeFormatter.ofPattern(iso8601));
    }
}
