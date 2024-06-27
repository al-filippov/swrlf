package ru.ulstu.is.ontology;

enum OntoAnnotation {
    FUZZY_INFERENCE("fuzzyInference"),
    FUZZY_INFERENCE_ACTIVATION("fuzzyInferenceActivation"),
    FUZZY_INFERENCE_CONJUNCTION("fuzzyInferenceConjunction"),
    FUZZY_INFERENCE_DISJUNCTION("fuzzyInferenceDisjunction"),
    FUZZY_INFERENCE_IMPLICATION("fuzzyInferenceImplication"),
    FUZZY_VARIABLE_MINIMUM("fuzzyVariableMinimum"),
    FUZZY_VARIABLE_MAXIMUM("fuzzyVariableMaximum"),
    FUZZY_INPUT_VARIABLE("fuzzyInputVariable"),
    FUZZY_OUTPUT_VARIABLE("fuzzyOutputVariable"),
    FUZZY_OUTPUT_VARIABLE_AGGREGATION("fuzzyOutputVariableAggregation"),
    FUZZY_OUTPUT_VARIABLE_DEFUZZIFIER("fuzzyOutputVariableDefuzzifier"),
    FUZZY_TERM("fuzzyTerm");

    private String name;

    private OntoAnnotation(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
