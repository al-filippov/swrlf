package ru.ulstu.is.utils;

public class FuzzyUtils {
    private FuzzyUtils() {
    }

    public static String escape(String str) {
        return str
                .replace("://", ".")
                .replace("/", ".")
                .replace("#", ".");
    }

    public static String shortView(String str) {
        final int sharpIndex = str.indexOf("#");
        if (sharpIndex == -1) {
            return str;
        }
        if (sharpIndex == str.length() - 1) {
            return str;
        }
        return str.substring(sharpIndex + 1);
    }
}
