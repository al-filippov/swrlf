package ru.ulstu.is.utils;

public class EnumUtils {
    private EnumUtils() {
    }

    public static <T extends Enum<T>> T findValue(Class<T> enumType, String name) {
        for (T value : enumType.getEnumConstants()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }
}
