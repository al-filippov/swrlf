package ru.ulstu.is.swrlf.parameter;

import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.defuzzifier.WeightedAverage;

public enum Defuzzifier {
    // TODO: Add more defuzzifier options
    CENTROID(new Centroid()),
    WEIGHTEDAVERAGE(new WeightedAverage());

    private com.fuzzylite.defuzzifier.Defuzzifier value;

    private Defuzzifier(com.fuzzylite.defuzzifier.Defuzzifier value) {
        this.value = value;
    }

    public com.fuzzylite.defuzzifier.Defuzzifier get() {
        return value;
    }
}
