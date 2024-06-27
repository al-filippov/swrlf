package ru.ulstu.is.ontology;

import java.util.Objects;

public class DataProperty {
    private final String name;
    private final Double value;
    private final Double min;
    private final Double max;
    private final String aggregation;
    private final String defuzzifier;

    public DataProperty(String name, Double value, Double min, Double max, String aggregation, String defuzzifier) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.aggregation = aggregation;
        this.defuzzifier = defuzzifier;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public String getAggregation() {
        return aggregation;
    }

    public String getDefuzzifier() {
        return defuzzifier;
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
        DataProperty other = (DataProperty) obj;
        return Objects.equals(name, other.name);
    }
}
