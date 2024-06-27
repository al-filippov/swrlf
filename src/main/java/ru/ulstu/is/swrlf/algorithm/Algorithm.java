package ru.ulstu.is.swrlf.algorithm;

import ru.ulstu.is.swrlf.parameter.Activation;
import ru.ulstu.is.swrlf.parameter.Aggregation;
import ru.ulstu.is.swrlf.parameter.Conjunction;
import ru.ulstu.is.swrlf.parameter.Defuzzifier;
import ru.ulstu.is.swrlf.parameter.Disjunction;
import ru.ulstu.is.swrlf.parameter.Implication;

public enum Algorithm {
    MAMDANI(new Settings(
            Activation.GENERAL,
            Conjunction.MINIMUM,
            Disjunction.MAXIMUX,
            Implication.MINIMUM,
            Aggregation.MAXIMUX,
            Defuzzifier.CENTROID)),
    TAKAGI_SUGENO(new Settings(
            Activation.GENERAL,
            Conjunction.NOT_USED,
            Disjunction.NOT_USED,
            Implication.NOT_USED,
            Aggregation.NOT_USED,
            Defuzzifier.WEIGHTEDAVERAGE)),
    TSUKAMOTO(new Settings(
            Activation.GENERAL,
            Conjunction.NOT_USED,
            Disjunction.NOT_USED,
            Implication.NOT_USED,
            Aggregation.NOT_USED,
            Defuzzifier.WEIGHTEDAVERAGE));

    private Settings settings;

    private Algorithm(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }
}
