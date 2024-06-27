package ru.ulstu.is.ontology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SwrlRule {
    private final String rule;
    private final String name;
    private final List<String> antecedent = new ArrayList<>();
    private final List<String> consequent = new ArrayList<>();

    public SwrlRule(String rule, String name, List<String> antecedent, List<String> consequent) {
        this.rule = rule;
        this.name = name;
        if (antecedent == null || antecedent.isEmpty()) {
            throw new InternalError("Antecedent can not be null or empty");
        }
        if (consequent == null || consequent.isEmpty()) {
            throw new InternalError("Consequent can not be null or empty");
        }
        this.antecedent.addAll(antecedent);
        this.consequent.addAll(consequent);
    }

    public List<String> getAntecedent() {
        return antecedent;
    }

    public List<String> getConsequent() {
        return consequent;
    }

    public String getRule() {
        return rule;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SwrlRule other = (SwrlRule) obj;
        return Objects.equals(rule, other.rule);
    }
}
