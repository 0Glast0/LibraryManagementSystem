package com.library.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LibraryUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public static String getCurrentDate() {
        return DATE_FORMAT.format(new Date());
    }

    public static String getReturnDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14); // 2 weeks borrowing period
        return DATE_FORMAT.format(calendar.getTime());
    }

    public static boolean isValidDate(String dateStr) {
        try {
            DATE_FORMAT.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && !name.matches(".*\\d.*");
    }

    public static String formatName(String name) {
        if (name == null || name.trim().isEmpty()) return "";
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static void validateBookInputs(String title, String author) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        if (author.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Author name cannot contain numbers");
        }
    }
}