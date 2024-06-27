package ru.ulstu.is.swrlf.algorithm;

import ru.ulstu.is.swrlf.parameter.Activation;
import ru.ulstu.is.swrlf.parameter.Aggregation;
import ru.ulstu.is.swrlf.parameter.Conjunction;
import ru.ulstu.is.swrlf.parameter.Defuzzifier;
import ru.ulstu.is.swrlf.parameter.Disjunction;
import ru.ulstu.is.swrlf.parameter.Implication;

public class Settings {
    private Activation activation;
    private Conjunction conjunction;
    private Disjunction disjunction;
    private Implication implication;
    private Aggregation aggregation;
    private Defuzzifier defuzzifier;

    Settings() {
    }

    Settings(
            Activation activation,
            Conjunction conjunction,
            Disjunction disjunction,
            Implication implication,
            Aggregation aggregation,
            Defuzzifier defuzzifier) {
        this.activation = activation;
        this.conjunction = conjunction;
        this.disjunction = disjunction;
        this.implication = implication;
        this.aggregation = aggregation;
        this.defuzzifier = defuzzifier;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public Conjunction getConjunction() {
        return conjunction;
    }

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;
    }

    public Disjunction getDisjunction() {
        return disjunction;
    }

    public void setDisjunction(Disjunction disjunction) {
        this.disjunction = disjunction;
    }

    public Implication getImplication() {
        return implication;
    }

    public void setImplication(Implication implication) {
        this.implication = implication;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public Defuzzifier getDefuzzifier() {
        return defuzzifier;
    }

    @Override
    public String toString() {
        return "activation=" + activation + ",\n"
                + "conjunction=" + conjunction + ",\n"
                + "disjunction=" + disjunction + ",\n"
                + "implication=" + implication + ",\n"
                + "aggregation=" + aggregation + ",\n"
                + "defuzzifier=" + defuzzifier;
    }
}
