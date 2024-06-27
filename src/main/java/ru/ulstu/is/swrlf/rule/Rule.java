package ru.ulstu.is.swrlf.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.ulstu.is.swrlf.term.Term;
import ru.ulstu.is.utils.FuzzyUtils;

public class Rule {
    private final String swrlRule;
    private final List<Term> antecedent = new ArrayList<>();
    private final List<Term> consequent = new ArrayList<>();

    public Rule(String swrlRule, List<Term> antecedent, List<Term> consequent) {
        this.swrlRule = swrlRule;
        if (antecedent == null || antecedent.isEmpty()) {
            throw new InternalError("Rule antecedent can not be null or empty");
        }
        if (consequent == null || consequent.isEmpty()) {
            throw new InternalError("Rule consequent can not be null or empty");
        }
        this.antecedent.addAll(antecedent);
        this.consequent.addAll(consequent);
    }

    private String termToAtom(Term term, Function<String, String> formatter) {
        return String.format("%s is %s",
                formatter.apply(term.getVariable()),
                formatter.apply(term.getName()));
    }

    private String termsToFuzzy(List<Term> terms, Function<String, String> formatter) {
        return terms.stream().map(term -> termToAtom(term, formatter)).collect(Collectors.joining(" and "));
    }

    private String formatRule(Function<String, String> formatter) {
        final String fuzzyAntecedent = termsToFuzzy(antecedent, formatter);
        final String fuzzyConsequent = termsToFuzzy(consequent, formatter);
        return String.format("if %s then %s", fuzzyAntecedent, fuzzyConsequent);
    }

    String getShort() {
        return formatRule(s -> FuzzyUtils.shortView(s));
    }

    public String get() {
        return formatRule(s -> FuzzyUtils.escape(s));
    }

    @Override
    public int hashCode() {
        return Objects.hash(swrlRule);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Rule other = (Rule) obj;
        return Objects.equals(swrlRule, other.swrlRule);
    }

    @Override
    public String toString() {
        return "SWRL rule: " + swrlRule + "\n"
                + "Fuzzy Rule: " + getShort();
    }
}
