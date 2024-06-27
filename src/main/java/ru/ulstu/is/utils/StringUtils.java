package ru.ulstu.is.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isBlank());
    }

    public static Double toDouble(String str) {
        return Optional.ofNullable(str).map(Double::parseDouble).orElse(null);
    }

    public static <T> String collectionToString(Collection<T> collection) {
        return collection.stream().map(item -> item.toString()).collect(Collectors.joining("\n"));
    }
}
