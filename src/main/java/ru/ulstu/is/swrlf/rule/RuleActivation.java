package ru.ulstu.is.swrlf.rule;

public class RuleActivation {
    private final Rule rule;
    private final Double activation;

    public RuleActivation(Rule rule, Double activation) {
        this.rule = rule;
        this.activation = activation;
    }

    public Rule getRule() {
        return rule;
    }

    public Double getActivation() {
        return activation;
    }

    @Override
    public String toString() {
        return "rule = " + rule.getShort() + ", activation = " + activation;
    }
}
