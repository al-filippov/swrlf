package ru.ulstu.is.ontology;

public class InferenceSettings {
    private final String algorithm;
    private final String activation;
    private final String conjunction;
    private final String disjunction;
    private final String implication;

    public InferenceSettings(
            String algorithm,
            String activation,
            String conjunction,
            String disjunction,
            String implication) {
        this.algorithm = algorithm;
        this.activation = activation;
        this.conjunction = conjunction;
        this.disjunction = disjunction;
        this.implication = implication;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getActivation() {
        return activation;
    }

    public String getConjunction() {
        return conjunction;
    }

    public String getDisjunction() {
        return disjunction;
    }

    public String getImplication() {
        return implication;
    }
}
