package ru.ulstu.is.ontology;

import java.util.Objects;

public class AnnotationProperty {
    private final String name;
    private final String value;

    public AnnotationProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationProperty other = (AnnotationProperty) obj;
        return Objects.equals(name, other.name);
    }
}
